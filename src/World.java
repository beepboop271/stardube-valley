import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

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

  public Shop generalStore; // TODO: make this private / move to area.readmap or sth like that after GS appears on the map

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

    this.generalStore = new Shop("GeneralStore");

    // spawn first day items
    this.doDayEndActions();
  }

  public void update() {
    long currentUpdateTime = System.nanoTime();
    this.processEvents();

    if (this.player.isInMenu()) {
      this.lastUpdateTime = currentUpdateTime;
      return;
    }
    this.inGameNanoTime += currentUpdateTime-this.lastUpdateTime;
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
          nextItemEntity.translatePos(nextItemEntity.getMove(currentUpdateTime-this.lastUpdateTime));
        }
      }
    }
    
    Iterator<Area> areas = this.locations.values().iterator();
    Area nextArea;
    Iterator<Moveable> moveables;
    Moveable nextMoveable;
    LinkedHashSet<Point> intersectingTiles;
    Vector2D move;
    int collideDirection;
    while (areas.hasNext()) {
      nextArea = areas.next();
      moveables = nextArea.getMoveables();

      while (moveables.hasNext()) {
        nextMoveable = moveables.next();
        move = nextMoveable.getMove(currentUpdateTime-this.lastUpdateTime);
        if (move != null) {
          intersectingTiles = nextMoveable.getIntersectingTiles(move.getXVector());
          collideDirection = nextArea.collides(intersectingTiles,
                                                   this.player.getPos().translateNew(move.getX(), 0),
                                                   true);
          if (collideDirection == World.EAST) {
            // subtract 0.0001 to prevent rounding from counting it as still colliding
            nextMoveable.translatePos(
                nextMoveable.getPos().round().x + 0.5-Player.SIZE - nextMoveable.getPos().x - 0.0001,
                0
            );
          } else if (collideDirection == World.WEST) {
            nextMoveable.translatePos(
                nextMoveable.getPos().round().x - 0.5+Player.SIZE - nextMoveable.getPos().x,
                0
            );
          } else if (collideDirection == -1) {
            nextMoveable.translatePos(move.getXVector());
          }

          intersectingTiles = nextMoveable.getIntersectingTiles(move.getYVector());
          collideDirection = nextArea.collides(intersectingTiles,
                                               this.player.getPos().translateNew(0, move.getY()),
                                               false);
          if (collideDirection == World.NORTH) {
            nextMoveable.translatePos(
                0,
                nextMoveable.getPos().round().y - 0.5+Player.SIZE - nextMoveable.getPos().y
            );
          } else if (collideDirection == World.SOUTH) {
            // subtract 0.0001 to prevent rounding from counting it as still colliding
            nextMoveable.translatePos(
                0,
                nextMoveable.getPos().round().y + 0.5-Player.SIZE - nextMoveable.getPos().y - 0.0001
            );
          } else if (collideDirection == -1) {
            nextMoveable.translatePos(move.getYVector());
          }
          
          if (nextMoveable instanceof Player) {
            this.playerArea = nextArea.moveAreas(nextMoveable, intersectingTiles.iterator());
          } else {
            nextArea.moveAreas(nextMoveable, intersectingTiles.iterator());
          }
        }
      }
    }

    this.lastUpdateTime = currentUpdateTime;
  }

  public void processEvents() {
    // pygame_irl
    EventObject event;
    // if (!this.eventQueue.isEmpty()) {
    //   System.out.print(this.eventQueue.size()+" ");
    //   System.out.println(this.eventQueue.peek());
    // }
    while (!this.eventQueue.isEmpty()
           && (this.eventQueue.peek().getTime() <= this.inGameNanoTime)) {
      event = this.eventQueue.poll().getEvent();
      if (event instanceof UtilityToolUsedEvent) {
        // design i think is solid, just need to clean up the code a bit?
        this.player.setImmutable(false);
        UtilityToolUsedEvent toolEvent = (UtilityToolUsedEvent)event;
        this.player.decreaseEnergy(((Tool)toolEvent.getHoldableUsed()).getEnergyCost());
        Tile selectedTile = this.playerArea.getMapAt(toolEvent.getLocationUsed());

        Point treePos = toolEvent.getLocationUsed().translateNew(2, 1);  // TODO: make this stuff less sketch
        Tile treeTile;
        if (this.playerArea.inMap(treePos)) {
          treeTile = this.playerArea.getMapAt(treePos);
        } else {
          treeTile = null;
        }

        // prioritize harvesting the actual tile before using the tree tile
        TileComponent componentToHarvest;
        if (selectedTile == null) {
          if (treeTile != null) {
            componentToHarvest = treeTile.getContent();
            if (!(componentToHarvest instanceof ExtrinsicTree)) {
              componentToHarvest = null;
            }
          } else {
            componentToHarvest = null;
          }
        } else {
          componentToHarvest = selectedTile.getContent();
          if (!(componentToHarvest instanceof ExtrinsicHarvestableComponent)) {
            if (treeTile != null && treeTile.getContent() instanceof ExtrinsicTree) {
              componentToHarvest = treeTile.getContent();
            } else {
              componentToHarvest = null;
            }
          }
        }

        if (componentToHarvest != null) {
          IntrinsicHarvestableComponent ic = ((IntrinsicHarvestableComponent)(((ExtrinsicHarvestableComponent)componentToHarvest).getIntrinsicSelf()));
          String requiredTool = ic.getRequiredTool();
                  
          if (requiredTool.equals("Any")
                || requiredTool.equals(toolEvent.getHoldableUsed().getName())) {
            // TODO: play breaking animation?
            ExtrinsicHarvestableComponent ec = ((ExtrinsicHarvestableComponent)componentToHarvest);
            if (ec.damageComponent(((UtilityTool)toolEvent.getHoldableUsed()).getEffectiveness())) {
              if (ec instanceof ExtrinsicTree) {
                if (((ExtrinsicTree)ec).getStage() == 17) {
                  ((ExtrinsicTree)ec).setStage(18);
                  ec.setHardnessLeft(5); // TODO: maybe dont hard code
                } else {
                  this.playerArea.removeComponentAt(treePos);
                }
              } else {
                this.playerArea.removeComponentAt(toolEvent.getLocationUsed());
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
             //TODO: make these tools not dependant on world
          }
        } else if (selectedTile instanceof GroundTile) {
          if (toolEvent.getHoldableUsed().getName().equals("WateringCan")
                &&  (((GroundTile)selectedTile).getTilledStatus() == true)) {
            ((GroundTile)selectedTile).setLastWatered(this.inGameDay);
            ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);

          } else if (selectedTile.getContent() == null) {
            if (toolEvent.getHoldableUsed().getName().equals("Hoe")) {
              if (this.playerArea instanceof FarmArea) {
                ((GroundTile)selectedTile).setTilledStatus(true);
                ((FarmArea)this.playerArea).addEditedTile((GroundTile)selectedTile);
              }
            } else if (toolEvent.getHoldableUsed().getName().equals("Pickaxe")) {
              if (((FarmArea)this.playerArea).hasTile((GroundTile)selectedTile)) { 
                ((GroundTile)selectedTile).setTilledStatus(false); 
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
              }
            }
          } else if (toolEvent.getHoldableUsed().getName().equals("Pickaxe")) {
              if ((this.playerArea instanceof FarmArea)
                    && ((FarmArea)this.playerArea).hasTile((GroundTile)selectedTile)) {
                ((FarmArea)this.playerArea).removeEditedTile((GroundTile)selectedTile);
                ((GroundTile)selectedTile).setTilledStatus(false); 
                selectedTile.setContent(null);
              }
            }
            ((GroundTile)selectedTile).determineImage(this.inGameDay);
          } //- Ground tile changes image based on what happened
      } else if (event instanceof PlayerInteractEvent) {
        //TODO: make this not just for forageables but also doors and stuff i guess
        int itemIndex = ((PlayerInteractEvent)event).getSelectedItemIndex();
        Point useLocation = ((PlayerInteractEvent)event).getLocationUsed();
        Gateway interactedGateway = this.playerArea.getGateway(useLocation);
        Tile currentTile = this.playerArea.getMapAt(useLocation);
        TileComponent[] bushContents = new TileComponent[3];
        if (this.playerArea.inMap(currentTile.getX()+1, currentTile.getY()) &&
            this.playerArea.inMap(currentTile.getX(), currentTile.getY()+1) &&
            this.playerArea.getMapAt(currentTile.getX()+1, currentTile.getY()) != null &&
            this.playerArea.getMapAt(currentTile.getX(), currentTile.getY()) != null &&
            this.playerArea.getMapAt(currentTile.getX()+1, currentTile.getY()) != null) {
          bushContents[0] = this.playerArea.getMapAt(
                            currentTile.getX()+1, currentTile.getY()).getContent();
          bushContents[1] = this.playerArea.getMapAt(
                            currentTile.getX()+1, currentTile.getY()+1).getContent();
          bushContents[2] = this.playerArea.getMapAt(
                            currentTile.getX(), currentTile.getY()+1).getContent();
        }
        if ((interactedGateway != null)
              && interactedGateway.requiresInteractToMove()
              && this.player.getPos().round().x == interactedGateway.getOrigin().x) {
          this.playerArea = this.playerArea.moveAreas(this.player, interactedGateway);
        } else if (currentTile != null) {

          if (currentTile.getContent() == null) {
            if ((this.player.hasAtIndex(itemIndex)) &&
                (this.player.getAtIndex(itemIndex)
                                        .getContainedHoldable() instanceof Consumable)) {
              this.player.consume();
            } 
          }

          //TODO: play foraging animation?
          TileComponent currentContent = currentTile.getContent();
          
          if (currentContent instanceof ExtrinsicCrop) {
            if (((ExtrinsicCrop)currentContent).canHarvest()) {
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
          } else if (currentContent instanceof ExtrinsicGrowableCollectable) {
            HoldableDrop productDrop = ((ExtrinsicGrowableCollectable)currentContent).getProduct();
            HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
            if (product !=null) {
              new HoldableStackEntity(product, null);
              if (this.player.canPickUp(product.getContainedHoldable())) {
                this.player.pickUp(product);
                ((ExtrinsicGrowableCollectable)currentContent).resetRegrowCooldown();
              }
            }
          } else if (bushContents[0] != null || bushContents[1] != null || bushContents[2] != null) {
            for (int i = 0; i < bushContents.length; i++) {
              if (bushContents[i] == null || bushContents[i] instanceof ExtrinsicCrop) {
                continue;
              } else if (bushContents[i] instanceof ExtrinsicGrowableCollectable) {
                HoldableDrop productDrop = ((ExtrinsicGrowableCollectable)bushContents[i]).getProduct();
                HoldableStack product = productDrop.resolveDrop(this.luckOfTheDay);
                if (product != null) {
                  new HoldableStackEntity(product, null);
                  if (this.player.canPickUp(product.getContainedHoldable())) {
                      this.player.pickUp(product);
                  ((ExtrinsicGrowableCollectable)bushContents[i]).resetRegrowCooldown();
                  } 
                }
              }
            }
          } else if (currentContent instanceof Collectable) {
            ((WorldArea)this.playerArea).setNumForageableTiles(((WorldArea)this.playerArea).getNumForageableTiles()-1);
            //TODO: make sure that when you create a new UtilityUsedEvent you check collectable
            HoldableDrop[] currentProducts = ((Collectable)currentContent).getProducts();
            HoldableStack drop = (currentProducts[0].resolveDrop(this.luckOfTheDay));
            if (drop != null) {
              new HoldableStackEntity(drop, null); // TODO: change the pos
              if (this.player.canPickUp(drop.getContainedHoldable())) {
                this.player.pickUp(drop);
                currentTile.setContent(null);
              }
            }
          } else if (currentContent instanceof ExtrinsicChest) {
            this.player.setCurrentInteractingMenuObj((ExtrinsicChest)currentContent);
            this.player.enterMenu(Player.CHEST_PAGE);
          } else if (currentContent instanceof ExtrinsicMachine) {
            if (((ExtrinsicMachine)currentContent).getProduct() != null) {
              if (this.player.canPickUp(((ExtrinsicMachine)currentContent)
                                          .getProduct().getContainedHoldable())) {
                this.player.pickUp(((ExtrinsicMachine)currentContent).getProduct());
                ((ExtrinsicMachine)currentContent).resetProduct();
              }
            } else if ((((ExtrinsicMachine)currentContent).getProduct() == null) && 
                        (((ExtrinsicMachine)currentContent).getItemToProcess() == null)){
              if (this.player.hasAtIndex(itemIndex)) {
                HoldableStack selectedItem = this.player.getAtIndex(itemIndex); //okay honestly this can be deleted this is just to make the next statement short
                if (((ExtrinsicMachine)currentContent).canProcess(
                                  selectedItem.getContainedHoldable().getName()) &&
                                  selectedItem.getQuantity() >= 
                                  ((ExtrinsicMachine)currentContent).getRequiredQuantity()) {
                  if (((ExtrinsicMachine)currentContent).getCatalyst() == null ||
                        this.player.hasHoldable(((ExtrinsicMachine)currentContent).getCatalyst())) {
                    ((ExtrinsicMachine)currentContent).setItemToProcess(
                        selectedItem.getContainedHoldable().getName());
                    ((ExtrinsicMachine)currentContent).increasePhase();   
                    player.decrementAtIndex(itemIndex, ((ExtrinsicMachine)currentContent).getRequiredQuantity());
                    player.decrementHoldable(1, ((ExtrinsicMachine)currentContent).getCatalyst());
                    this.emplaceFutureEvent(
                        ((ExtrinsicMachine)currentContent).getProcessingTime(
                            selectedItem.getContainedHoldable().getName()), 
                            new MachineProductionFinishedEvent((ExtrinsicMachine)currentContent));   
                  }                                   
                }
              } 
            } 
          } else if (currentContent instanceof ShippingContainer) {
            if (this.player.hasAtIndex(itemIndex)) {
              if (!(this.player.getAtIndex(itemIndex).getContainedHoldable() instanceof Tool)) {
                HoldableStack currentItem = this.player.getAtIndex(itemIndex);
                this.player.increaseFutureFunds(((ShippingContainer)currentContent).sellItem(currentItem));
                this.player.removeAtIndex(itemIndex);
              }
            }
          }
        }
      } else if (event instanceof MachineProductionFinishedEvent) {
        ((ExtrinsicMachine)event.getSource()).processItem();

      } else if (event instanceof CastingEndedEvent) {
        FishingRod rodUsed = ((CastingEndedEvent)event).getRodUsed(); // TODO: send into the fishing game as a parameter
        this.player.decreaseEnergy(rodUsed.getEnergyCost());
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
            this.player.setImmutable(false);
          }
        } else {
          this.player.setImmutable(false);
        }
        
      } else if (event instanceof CatchFishEvent) {
        FishingRod rodUsed = ((CatchFishEvent)event).getRodUsed();
        long catchNanoTime = ((CatchFishEvent)event).getCatchNanoTime();
        if ((catchNanoTime >= this.player.getCurrentFishingGame().getBiteNanoTime()) &&
            (catchNanoTime <= this.player.getCurrentFishingGame().getBiteNanoTime()+FishingGame.BITE_ELAPSE_NANOTIME)) {
          // as luck increases, chance to get trash decreases (minimum chance to get a fish is 25%)
          if ((rodUsed.getTileToFish().getFishableFish().length==0)
              || (Math.random() >= (this.luckOfTheDay+0.25))) {
            Holdable trashEarned = HoldableFactory.getHoldable(
                                   WaterTile.getFishableTrash()[(int)(Math.round(Math.random()*(WaterTile.getFishableTrash().length-1)))]);
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
        //- Make sure the player literally has the object
        if (this.player.hasAtIndex(((ComponentPlacedEvent)event).getComponentIndex())) {
          Tile currentTile = this.playerArea.getMapAt(((ComponentPlacedEvent)event)
                                                      .getLocationUsed());
          TileComponent currentContent = currentTile.getContent();
          //- Anything that you can place must not be placed over something and not over water
          if ((currentContent == null) && (!(currentTile instanceof WaterTile))) { 
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
              this.player.useAtIndex(((ComponentPlacedEvent)event).getComponentIndex());
            }
          }
        }
      }
    }
  }

  public void doDayEndActions() {
    this.player.recover(this.inGameNanoTime);
    // day starts at 6 am
    this.inGameNanoTime = 6*60*1_000_000_000L;
    ++this.inGameDay;
    if ((this.inGameDay % DAYS_PER_SEASON == 1) && (this.inGameDay > 1)) {
      this.inGameSeason = (this.inGameSeason + 1) % 4;
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

    this.player.increaseCurrentFunds(this.player.getFutureFunds()); //TODO: make this a cool menu screen
  }

  public void queueEvent(TimedEvent te) {
    this.eventQueue.offer(te);
  }

  public void emplaceEvent(long time, EventObject event) {
    this.eventQueue.offer(new TimedEvent(time, event));
  }

  public void emplaceFutureEvent(long nanoTimeIntoFuture, EventObject event) {
    // long time = this.inGameNanoTime+nanoTimeIntoFuture;
    // System.out.printf("enqueue t=%02d:%02d:%d\n", time/60, time%60, (this.inGameNanoTime+nanoTimeIntoFuture)%1_000_000_000);
    // long start = System.nanoTime();
    this.eventQueue.offer(new TimedEvent(this.inGameNanoTime+nanoTimeIntoFuture, event));
    // System.out.printf("enq took %d ms\n", (System.nanoTime()-start)/1_000_000);
  }

  public void loadAreas() throws IOException {
    String[] splitLine;
    String nextLine;

    // declare all areas first, then fill them in later
    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/map/Areas"));
    nextLine = input.readLine();
    while (nextLine != null) {
      splitLine = nextLine.split(" ");
      this.locations.put(splitLine[1],
                         Area.constructArea(splitLine[0], splitLine[1],
                                            Integer.parseInt(splitLine[2]),
                                            Integer.parseInt(splitLine[3])));
      nextLine = input.readLine();
    }
    input.close();

    // load the tiles
    Iterator<Area> locationAreas = this.locations.values().iterator();
    while (locationAreas.hasNext()) {
      this.loadAreaMap(locationAreas.next());
    }

    // add gateways
    input = new BufferedReader(new FileReader("assets/gamedata/map/Gateways"));
    nextLine = input.readLine();
    // (\w++) ?             group 1: area1 name, optionally followed by space
    // \((\d++), ?(\d++)\)  group 2,3: coordinates of gateway in area1, with parentheses and optional space
    // \s++(I?)<->(I?)\s++  group 4,5: any number of spaces surrounding <-> and whether or not interaction is required to travel
    // (\w++) ?             group 6: area2 name, optionally followed by space
    // \((\d++), ?(\d++)\)  group 7,8: coordinates of gateway in area2, with parentheses and optional space
    //  ?(\\w++)*           group 9: name of the building for the second area to travel into (some will not have a building, in which they get "none")
    Pattern gatewayPattern = Pattern.compile("^(\\w++) ?\\((\\d++), ?(\\d++)\\)\\s++(I?)<->(I?)\\s++(\\w++) ?\\((\\d++), ?(\\d++)\\) ?(\\w++)$");
    Matcher gatewayMatch;
    Gateway gateway1, gateway2;
    while (!nextLine.equals("")) {
      gatewayMatch = gatewayPattern.matcher(nextLine);
      gatewayMatch.matches();
      gateway1 = new Gateway(Integer.parseInt(gatewayMatch.group(2)),
                             Integer.parseInt(gatewayMatch.group(3)),
                             World.NORTH,
                             gatewayMatch.group(5).equals("I"));
      gateway2 = new Gateway(Integer.parseInt(gatewayMatch.group(7)),
                             Integer.parseInt(gatewayMatch.group(8)),
                             World.SOUTH,
                             gatewayMatch.group(4).equals("I"));

      gateway1.setDestinationArea(this.locations.get(gatewayMatch.group(6)));
      gateway1.setDestinationGateway(gateway2);
      gateway2.setDestinationArea(this.locations.get(gatewayMatch.group(1)));
      gateway2.setDestinationGateway(gateway1);

      if (!(gatewayMatch.group(9).equals("none"))) {
        this.locations.get(gatewayMatch.group(1)).getMapAt(gateway1.getOrigin()).setContent(
                              IntrinsicTileComponentFactory.getComponent(gatewayMatch.group(9)));
      }

      this.locations.get(gatewayMatch.group(1)).addGateway(gateway1);
      this.locations.get(gatewayMatch.group(6)).addGateway(gateway2);
      nextLine = input.readLine();
    }
    input.close();

    // add gateway zones
    input = new BufferedReader(new FileReader("assets/gamedata/map/Connections"));
    nextLine = input.readLine();
    while (nextLine != null) {
      splitLine = input.readLine().split(" ");
      for (int i = 0; i < 4; ++i) {
        if (!splitLine[i].equals("null")) {
          this.locations.get(nextLine)
              .getNeighbourZone(i)
              .setDestinationArea(this.locations.get(splitLine[i]));
        }
      }
      nextLine = input.readLine();
    }
    input.close();
  }

  public void loadAreaMap(Area a) throws IOException {
    //System.out.println(a.getName());
    BufferedReader input = new BufferedReader(new FileReader("assets/maps/"
                                                             + a.getName()));
    String nextLine;

    
    System.out.println("Initializing " + a.getName());
    for (int y = 0; y < a.getHeight(); ++y) {
      //System.out.println("on row" + y);
      nextLine = input.readLine();
      for (int x = 0; x < a.getWidth(); ++x) {
       // System.out.println("on col" + x);
        switch (nextLine.charAt(x)) {
          case '.':
            a.setMapAt(new GroundTile(x, y));
            break;
          case ',':
            a.setMapAt(new UnwalkableGround(x, y));
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
          case '-':
            a.setMapAt(new PlankTile(x, y));
            break;
          case 'b':
            a.setMapAt(new DecorationTile(x, y,nextLine.charAt(x)));
            break;
          case 'f':
            a.setMapAt(new DecorationTile(x, y,nextLine.charAt(x)));
            break;
          case 's':
            Tile createdTile = new GroundTile(x, y);
            createdTile.setContent(IntrinsicTileComponentFactory.getComponent("ShippingContainer"));
            a.setMapAt(createdTile);
            break;
          case 'g':
            a.setMapAt(new GroundTile(x, y));
            break;
        }
      }
    }

    a.setNeighbourZone(World.WEST,
                       World.findNeighbourZone(a, 0, 1, World.WEST));
    a.setNeighbourZone(World.EAST,
                       World.findNeighbourZone(a, a.getWidth()-1, 1, World.EAST));
    a.setNeighbourZone(World.NORTH,
                       World.findNeighbourZone(a, 1, 0, World.NORTH));
    a.setNeighbourZone(World.SOUTH,
                       World.findNeighbourZone(a, 1, a.getHeight()-1, World.SOUTH));

    input.close();
  }
  
  public static GatewayZone findNeighbourZone(Area a,
                                              int x, int y,
                                              int orientation) {
    if (orientation == World.NORTH || orientation == World.SOUTH) {
      while (x < a.getWidth()-1 && a.getMapAt(x, y) == null) {
        ++x;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, orientation);
      }
    } else {
      while (y < a.getHeight()-1 && a.getMapAt(x, y) == null) {
        ++y;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, orientation);
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