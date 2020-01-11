import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Timer;

/**
 * [World]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */

public class World {
  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;
  public static final int getOppositeDirection(int direction) {
    // can be moved wherever idc just needed to quickly write
    if (direction == World.NORTH) {
      return World.SOUTH;
    } else if (direction == World.EAST) {
      return World.WEST;
    } else if (direction == World.SOUTH) {
      return World.NORTH;
    } else if (direction == World.WEST) {
      return World.EAST;
    } else {
      throw new IllegalArgumentException("not a valid direction");
    }
  }

  private static final String[] SEASONS = {"Spring", "Summer", "Fall", "Winter"};
  private static final int DAYS_PER_SEASON = 28;

  private LinkedHashMap<String, Area> locations;
  private PriorityBlockingQueue<TimedEvent> eventQueue;
  private Area playerArea;
  private Player player;
  private long lastUpdateTime = System.nanoTime();
  private long inGameNanoTime;
  private long inGameDay;
  private int inGameSeason;
  private double luckOfTheDay;
  private HashMap<Player, Timer> fishingTimers;

  public World() {
    this.locations = new LinkedHashMap<String, Area>();
    try {
      this.loadAreas();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.eventQueue = new PriorityBlockingQueue<TimedEvent>();
    
    this.player = new Player(new Point(13, 13), "assets/gamedata/PlayerImages");
    this.playerArea = this.locations.get("Farm");
    this.playerArea.addMoveable(this.player);

    this.inGameDay = 0;
    this.inGameSeason = 0;

    // spawn first day items
    this.doDayEndActions();
  }

  public void update() {
    Iterator<Area> areas = this.locations.values().iterator();
    Area nextArea;
    Iterator<Moveable> moveables;
    Moveable nextMoveable;
    LinkedHashSet<Point> intersectingTiles;
    Point lastPos;
    int exitDirection;

    long currentUpdateTime = System.nanoTime();
    this.processEvents();

    if (this.player.isInMenu()) {
      this.lastUpdateTime = currentUpdateTime;
      return;
    }
    
    this.inGameNanoTime += (currentUpdateTime-this.lastUpdateTime);
    this.inGameNanoTime %= 24*60*1_000_000_000L;

    // check for end of day
    if (this.inGameNanoTime >= 2*60*1_000_000_000L && this.inGameNanoTime <= 6*60*1_000_000_000L) {
      this.doDayEndActions();
    }

    synchronized (this.playerArea.getItemsOnGroundList()) {
      Iterator<HoldableStackEntity> itemsNearPlayer = this.playerArea.getItemsOnGround();
      HoldableStackEntity nextItemEntity;
      double itemDistance;
      while (itemsNearPlayer.hasNext()) {
        nextItemEntity = itemsNearPlayer.next();
        itemDistance = nextItemEntity.getPos().distanceTo(this.player.getPos());
        if (itemDistance < Player.SIZE) {
          this.player.pickUp(nextItemEntity.getStack());
          itemsNearPlayer.remove();
        } else if (itemDistance < Player.getItemAttractionDistance()) {
          nextItemEntity.setVelocity(this.player.getPos().x-nextItemEntity.getPos().x,
                                    this.player.getPos().y-nextItemEntity.getPos().y, 
                                    (double)Player.getItemAttractionDistance()/itemDistance);
          nextItemEntity.makeMove(currentUpdateTime-this.lastUpdateTime);
        }
      }
    }

    while (areas.hasNext()) {
      nextArea = areas.next();
      moveables = nextArea.getMoveables();

      while (moveables.hasNext()) {
        nextMoveable = moveables.next();
        lastPos = nextMoveable.getPos();
        nextMoveable.makeMove(currentUpdateTime-this.lastUpdateTime);

        intersectingTiles = nextMoveable.getIntersectingTiles();
        // System.out.println(intersectingTiles);
        if (nextArea.collides(intersectingTiles.iterator())) {
          nextMoveable.setPos(lastPos);
        } else {
          exitDirection = nextArea.canMoveAreas(intersectingTiles.iterator());
          if (exitDirection > -1) {
            if (nextMoveable instanceof Player) {
              this.playerArea = nextArea.moveAreas(nextMoveable, exitDirection);
            } else {
              nextArea.moveAreas(nextMoveable, exitDirection);
            }
          }
        }
      }
    }
    //// testing TODO: remove
    nextArea = this.playerArea;
    moveables = nextArea.getMoveables();
    while (moveables.hasNext()) {
      nextMoveable = moveables.next();
      lastPos = nextMoveable.getPos();
      nextMoveable.makeMove(currentUpdateTime-this.lastUpdateTime);
      intersectingTiles = nextMoveable.getIntersectingTiles();
      if (nextArea.collides(intersectingTiles.iterator())) {
        nextMoveable.setPos(lastPos);
      }
    }
    ////

    this.lastUpdateTime = currentUpdateTime;
  }

  public void processEvents() {
    // pygame_irl
    EventObject event;
    while (!this.eventQueue.isEmpty()
           && (this.eventQueue.peek().getTime() <= this.inGameNanoTime)) {
      event = this.eventQueue.poll().getEvent();
      if (event instanceof UtilityToolUsedEvent) {
        // design i think is solid, just need to clean up the code a bit?
        this.player.setImmutable(false);
        UtilityToolUsedEvent toolEvent = (UtilityToolUsedEvent)event;
        Tile selectedTile = this.playerArea.getMapAt(toolEvent.getLocationUsed());
        int treeX = selectedTile.getX() + 2;  // TODO: make this stuff less sketch
        int treeY = selectedTile.getY() + 1;
        Tile treeTile = this.playerArea.getMapAt(treeX, treeY);
        TileComponent treeComponent = treeTile.getContent();
        TileComponent componentToHarvest = selectedTile.getContent();

        if (treeComponent instanceof ExtrinsicHarvestableComponent) {
          IntrinsicHarvestableComponent ic = ((IntrinsicHarvestableComponent)(((ExtrinsicHarvestableComponent)treeComponent).getIntrinsicSelf()));
          String requiredTool = ic.getRequiredTool();
                  
          if (requiredTool.equals("Axe")) {
            // TODO: play breaking animation?
            ExtrinsicHarvestableComponent ec = ((ExtrinsicHarvestableComponent)treeComponent);
            if (ec.damageComponent(((UtilityTool)toolEvent.getHoldableUsed()).getEffectiveness())) {
              Point point = new Point(toolEvent.getLocationUsed().x+2, toolEvent.getLocationUsed().y+1);
              if (this.playerArea instanceof WorldArea) {
                ExtrinsicTree tree = ((ExtrinsicTree)((WorldArea)this.playerArea).getMapAt(point).getContent());
                if (tree.getStage() == 17) {
                  tree.setStage(18);
                } else {
                  this.playerArea.removeComponentAt(point);
                }
              } else if (this.playerArea instanceof FarmArea) {
                ExtrinsicTree tree = ((ExtrinsicTree)((FarmArea)this.playerArea).getMapAt(point).getContent());
                if (tree.getStage() == 17) {
                  tree.setStage(18);
                } else {
                  this.playerArea.removeComponentAt(point);
                }
              }
              

              HoldableDrop[] drops = ic.getProducts();
              HoldableStack product;
              for (int i = 0; i < drops.length; ++i) {
                product = drops[i].resolveDrop(this.luckOfTheDay);
                if (product != null) {
                  this.playerArea.addItemOnGround(
                    new HoldableStackEntity(
                        product,
                        toolEvent.getLocationUsed().translateNew(Math.random()-0.5, Math.random()-0.5)
                    )
                  );
                }
              }
            }
          }
        } else if (componentToHarvest instanceof ExtrinsicHarvestableComponent) {
          IntrinsicHarvestableComponent ic = ((IntrinsicHarvestableComponent)(((ExtrinsicHarvestableComponent)componentToHarvest).getIntrinsicSelf()));
          String requiredTool = ic.getRequiredTool();
                  
          if (requiredTool.equals("Any")
                || requiredTool.equals(toolEvent.getHoldableUsed().getName())) {
            // TODO: play breaking animation?
            ExtrinsicHarvestableComponent ec = ((ExtrinsicHarvestableComponent)componentToHarvest);
            if (ec.damageComponent(((UtilityTool)toolEvent.getHoldableUsed()).getEffectiveness())) {
              this.playerArea.removeComponentAt(toolEvent.getLocationUsed());

              HoldableDrop[] drops = ic.getProducts();
              HoldableStack product;
              for (int i = 0; i < drops.length; ++i) {
                product = drops[i].resolveDrop(this.luckOfTheDay);
                if (product != null) {
                  this.playerArea.addItemOnGround(
                    new HoldableStackEntity(
                        product,
                        toolEvent.getLocationUsed().translateNew(Math.random()-0.5, Math.random()-0.5)
                    )
                  );
                }
              }
            }
             //TODO: make these tools not dependant on world
          }
        } else if (selectedTile instanceof GroundTile) {
          if (toolEvent.getHoldableUsed().getName().equals("WateringCan") && 
              (((GroundTile)selectedTile).getTilledStatus() == true)) {
                ((GroundTile)selectedTile).setLastWatered(this.inGameDay);
              ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);

          } else if (selectedTile.getContent() == null) {
            if (toolEvent.getHoldableUsed().getName().equals("Hoe")) {
              if (this.playerArea instanceof FarmArea) {
                ((GroundTile)selectedTile).setTilledStatus(true);
                ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);
              }
            } else if (toolEvent.getHoldableUsed().getName().equals("Pickaxe")) {
              ((GroundTile)selectedTile).setTilledStatus(false); 
              if ((this.playerArea instanceof FarmArea)
                    && ((FarmArea)this.playerArea).hasTile((GroundTile)selectedTile)) {
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
              }
            }
          } else {
            if (toolEvent.getHoldableUsed().getName().equals("Pickaxe")) {
              if (selectedTile.getContent() instanceof ExtrinsicCrop) {
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
              }
              selectedTile.setContent(null);
            }
          } //- Ground tile changes image based on what happened
          ((GroundTile)selectedTile).determineImage(this.inGameDay);
        }
      } else if (event instanceof UtilityUsedEvent) {
        //TODO: make this not just for forageables but also doors and stuff i guess
        Tile currentTile = this.playerArea.getMapAt(((UtilityUsedEvent) event).getLocationUsed());
        if (this.playerArea instanceof WorldArea) {
          ((WorldArea)this.playerArea).numForageableTiles--;
        }
        //TODO: play foraging animation?
        TileComponent currentContent = currentTile.getContent();
        if (currentContent instanceof ExtrinsicCrop) {
          if (((ExtrinsicCrop)currentContent).canHarvest()) {
            System.out.println(((ExtrinsicCrop)currentContent).getProduct());
            HoldableDrop productDrop = ((ExtrinsicCrop)currentContent).getProduct();
            HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
            if ((product != null)
                  && this.player.canPickUp(product.getContainedHoldable())) {
              this.player.pickUp(product);
              if (((ExtrinsicCrop)currentContent).shouldRegrow()) {
                ((ExtrinsicCrop)currentContent).resetRegrowCooldown();
              } else {
                currentTile.setContent(null);
              }
            }
          }
        } else if (currentContent instanceof Collectable) {
        //TODO: make sure that when you create a new UtilityUsedEvent you check collectable
          HoldableDrop[] currentProducts = ((Collectable)currentContent).getProducts();
          // also for some reason the above is sometimes null and i don't know why :D
          HoldableStack drop = (currentProducts[0].resolveDrop(this.luckOfTheDay));
          if (drop != null) {
            new HoldableStackEntity(drop, null); // TODO: change the pos
            if (this.player.canPickUp(drop.getContainedHoldable())) {
              this.player.pickUp(drop);
              currentTile.setContent(null);
            }
          }
        }
      } else if (event instanceof CastingEndedEvent) {
        FishingRod rodUsed = ((CastingEndedEvent)event).getRodUsed(); // TODO: send into the fishing game as a parameter
        int meterPercentage = ((CastingEndedEvent)event).getMeterPercentage();
        int castDistance = (int)(Math.round(FishingRod.MAX_CASTING_DISTANCE*(meterPercentage/100.0)));
        Point roundedPlayerPos = player.getPos().round();
        int destX = (int)roundedPlayerPos.x;
        int destY = (int)roundedPlayerPos.y;
        if (this.player.getOrientation() == World.NORTH) {
          destY -= castDistance;
        } else if (this.player.getOrientation() == World.SOUTH) {
          destY += castDistance;
        } else if (this.player.getOrientation() == World.WEST) {
          destX -= castDistance;
        } else {
          destX += castDistance;
        }
        if (playerArea.hasValidXYAt(destX, destY)) {
          if (playerArea.getMapAt(destX, destY) instanceof WaterTile) {
            rodUsed.setTileToFish((WaterTile)(playerArea.getMapAt(destX, destY)));
            rodUsed.setCurrentStatus(FishingRod.WAITING_STATUS);
            this.player.setCurrentFishingGame(new FishingGame(rodUsed.getTileToFish()));
          } else {
            // TODO: play animation
            this.player.setImmutable(false);
          }
        } else {
          // TODO: play animation
          this.player.setImmutable(false);
        }
        
      } else if (event instanceof CatchFishEvent) {
        FishingRod rodUsed = ((CatchFishEvent)event).getRodUsed();
        long catchNanoTime = ((CatchFishEvent)event).getCatchNanoTime();
        if ((catchNanoTime >= this.player.getCurrentFishingGame().getBiteNanoTime()) &&
            (catchNanoTime <= this.player.getCurrentFishingGame().getBiteNanoTime()+FishingGame.BITE_ELAPSE_NANOTIME)) {
          if ((rodUsed.getTileToFish().getFishableFish().length==0)
              || ((Math.random()*100) <= 30)) { // TODO: make this associated with luck
            Holdable trashEarned = HoldableFactory.getHoldable(
                                   WaterTile.getFishableTrash()[(int)(Math.round(Math.random()*WaterTile.getFishableTrash().length))]);
            if (this.player.canPickUp(trashEarned)) {
              this.player.pickUp(new HoldableStack(trashEarned, 1));
            }
            this.player.endCurrentFishingGame();
            this.player.setImmutable(false);
          } else {
            this.player.getCurrentFishingGame().setHasStarted(true);
          }
        } else {
          this.player.endCurrentFishingGame();
          this.player.setImmutable(false);
        }
        rodUsed.setCurrentStatus(FishingRod.IDLING_STATUS);
      } else if (event instanceof FishingGameEndedEvent) {
        FishingGame gameEnded = ((FishingGameEndedEvent)event).getGameEnded();
        if (gameEnded.getCurrentStatus() == FishingGame.WIN_STATUS) {
          Holdable fishEarned = ((FishingGameEndedEvent)event).getFishReturned();
          if (this.player.canPickUp(fishEarned)) {
            this.player.pickUp(new HoldableStack(fishEarned, 1));
          }
        }
        this.player.setImmutable(false);

      } else if (event instanceof ComponentPlacedEvent) {
        Tile currentTile = this.playerArea.getMapAt(((ComponentPlacedEvent)event).getLocationUsed());
        TileComponent currentContent = currentTile.getContent();
        if (currentContent == null) { //- Anything that you can place must not be placed over something
          //- We need to make sure that the tile is both a ground tile and is tilled if
          //- we're trying to plant a crop in that tile
          if (((ComponentPlacedEvent)event).getComponentToPlace() instanceof ExtrinsicCrop) {
            if (currentTile instanceof GroundTile) {
              if (((GroundTile)currentTile).getTilledStatus()) {
                if (this.playerArea instanceof FarmArea) {
                  currentTile.setContent(((ComponentPlacedEvent)event).getComponentToPlace());
                  ((FarmArea)this.playerArea).addEditedTile((GroundTile)currentTile);
                  this.player.useAtIndex(((ComponentPlacedEvent)event).getComponentIndex());
                }
              }
            }
          } else { //- If it's not a crop, you can place it anywhere
            currentTile.setContent(((ComponentPlacedEvent)event).getComponentToPlace());
          }
        }
      }
    }
  }

  public void doDayEndActions() {
    // day starts at 6 am
    this.inGameNanoTime = 6*60*1_000_000_000L;
    ++this.inGameDay;
    if ((this.inGameDay % DAYS_PER_SEASON == 1) && (this.inGameDay > 1)) {
      this.inGameSeason = (this.inGameSeason + 1) % 3;
    }
    this.luckOfTheDay = Math.random();
    Iterator<Area> areas = this.locations.values().iterator();
    Area nextArea;
    while (areas.hasNext()) {
      nextArea = areas.next();
      nextArea.updateDay(); //- someone can improve this i guess
      nextArea.updateSeason();
      nextArea.doDayEndActions();
    }
  }

  public void queueEvent(TimedEvent te) {
    this.eventQueue.offer(te);
  }

  public void emplaceEvent(long time, EventObject event) {
    this.eventQueue.offer(new TimedEvent(time, event));
  }

  public void emplaceFutureEvent(long nanoTimeIntoFuture, EventObject event) {
    this.eventQueue.offer(new TimedEvent(this.inGameNanoTime+nanoTimeIntoFuture, event));
  }

  public void loadAreas() throws IOException {
    String[] areaInfo;
    String nextLine;

    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/map/Areas"));
    nextLine = input.readLine();
    while (nextLine != null) {
      areaInfo = nextLine.split(" ");
      this.locations.put(areaInfo[1],
                         Area.constructArea(areaInfo[0], areaInfo[1],
                                            Integer.parseInt(areaInfo[2]),
                                            Integer.parseInt(areaInfo[3])));
      nextLine = input.readLine();
    }
    input.close();

    Iterator<Area> locationAreas = this.locations.values().iterator();
    while (locationAreas.hasNext()) {
      World.loadAreaMap(locationAreas.next());
    }

    input = new BufferedReader(new FileReader("assets/gamedata/map/Connections"));
    nextLine = input.readLine();
    while (nextLine != null) {
      areaInfo = input.readLine().split(" ");
      for (int i = 0; i < 4; ++i) {
        if (!areaInfo[i].equals("null")) {
          this.locations.get(nextLine)
              .getNeighbourZone(i)
              .initializeDestination(this.locations.get(areaInfo[i]), i);
        }
      }
      nextLine = input.readLine();
    }
    input.close();
  }

  public static void loadAreaMap(Area a) throws IOException {
    BufferedReader input = new BufferedReader(new FileReader("assets/maps/"
                                                             + a.getName()));
    String nextLine;

    for (int y = 0; y < a.getHeight(); ++y) {
      nextLine = input.readLine();
      for (int x = 0; x < a.getWidth(); ++x) {
        switch (nextLine.charAt(x)) {
          case '.':
            a.setMapAt(new GroundTile(x, y));
            break;
          case 'x':
            a.setMapAt(new GrassTile(x, y));
            break;
          case 'p':
            a.setMapAt(new PondTile(x, y));
            break;
          case 'r':
            a.setMapAt(new RiverTile(x, y));
            break;
          case 'l':
            a.setMapAt(new LakeTile(x, y));
            break;
          case 'o':
            a.setMapAt(new OceanTile(x, y));
            break;
          case 'b':
            a.setMapAt(new PathTile(x, y));
            break;
        }
      }
    }

    a.setNeighbourZone(World.WEST,
                       World.findNeighbourZone(a, 0, 1, false));
    a.setNeighbourZone(World.EAST,
                       World.findNeighbourZone(a, a.getWidth()-1, 1, false));
    a.setNeighbourZone(World.NORTH,
                       World.findNeighbourZone(a, 1, 0, true));
    a.setNeighbourZone(World.SOUTH,
                       World.findNeighbourZone(a, 1, a.getHeight()-1, true));

    input.close();
  }
  //shoot me
  public static WorldGate findNeighbourZone(Area a,
                                              int x, int y,
                                              boolean isHorizontal) {
    if (isHorizontal) {
      while (x < a.getWidth()-1 && a.getMapAt(x, y) == null) {
        ++x;
      }
      if (a.getMapAt(x, y) != null) {
        return new WorldGate(x, y, isHorizontal);
      }
    } else {
      while (y < a.getHeight()-1 && a.getMapAt(x, y) == null) {
        ++y;
      }
      if (a.getMapAt(x, y) != null) {
        return new WorldGate(x, y, isHorizontal);
      }
    }
    return null;
  }

  public Player getPlayer() {
    return this.player;
  }

  public Area getPlayerArea() {
    return this.playerArea;
  }

  public void setPlayerArea(Area a) {
    // testing only
    this.playerArea = a;
    // TODO: die
  }

  public long getInGameNanoTime() {
    return this.inGameNanoTime;
  }

  public long getInGameDay() {
    return this.inGameDay;
  }

  public int getInGameSeason() {
    return this.inGameSeason;
  }

  public static String[] getSeasons() {
    return World.SEASONS;
  }

  public static int getDaysPerSeason() {
    return DAYS_PER_SEASON;
  }
}