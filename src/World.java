import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * [World]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Joseph Wang
 */
public class World {
  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;

  private static final String[] seasons = {"Spring", "Summer", "Fall", "Winter"};

  private static final int DAYS_PER_SEASON = 28;

  private LinkedHashMap<String, Area> locations;
  private PriorityBlockingQueue<TimedEvent> eventQueue;
  private Area playerArea;
  private Player player;
  private long lastUpdateTime = System.nanoTime();
  private long inGameNanoTime;
  private long inGameDay = 0;
  private int inGameSeason;
  private double luckOfTheDay;

  public World() {
    this.locations = new LinkedHashMap<String, Area>();
    try {
      this.loadAreas();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.eventQueue = new PriorityBlockingQueue<TimedEvent>();
    
    this.player = new Player(new Point(13, 13));
    this.playerArea = this.locations.get("Farm");
    this.playerArea.addMoveable(this.player);

    this.inGameSeason = 1;

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

    this.player.updateCurrentFishingGame();


    if (this.player.isInMenu()) {
      this.lastUpdateTime = currentUpdateTime;
      return;
    }

    this.inGameNanoTime += currentUpdateTime-this.lastUpdateTime;
    this.inGameNanoTime %= (long)24*60*1_000_000_000;

    Iterator<HoldableStackEntity> itemsNearPlayer = this.playerArea.getItemsOnGround();
    HoldableStackEntity nextItemEntity;
    double itemDistance;
    while (itemsNearPlayer.hasNext()) {
      nextItemEntity = itemsNearPlayer.next();
      itemDistance = nextItemEntity.getPos().distanceTo(this.player.getPos());
      if (itemDistance < Player.getSize()) {
        this.player.pickUp(nextItemEntity.getStack());
      } else if (itemDistance < Player.getItemAttractionDistance()) {
        nextItemEntity.setVelocity(this.player.getPos().x-nextItemEntity.getPos().x,
                                   this.player.getPos().y-nextItemEntity.getPos().y, 
                                   (double)Player.getItemAttractionDistance()/itemDistance);
        nextItemEntity.makeMove(currentUpdateTime-this.lastUpdateTime);
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
        TileComponent componentToHarvest = selectedTile.getContent();

        if (componentToHarvest instanceof Harvestable
              && (((Harvestable)componentToHarvest).getRequiredTool().equals("Any")
                  || ((Harvestable)componentToHarvest).getRequiredTool().equals(
                        toolEvent.getHoldableUsed().getName()))) {
          // TODO: play breaking animation?
          this.playerArea.removeComponentAt(toolEvent.getLocationUsed());

          HoldableDrop[] drops = ((Harvestable)componentToHarvest).getProducts();
          for (int i = 0; i < drops.length; ++i) {
            this.playerArea.addItemOnGround(
                new HoldableStackEntity(
                    drops[i].resolveDrop(this.luckOfTheDay),
                    toolEvent.getLocationUsed().translateNew(Math.random()*2-1, Math.random()*2-1)
                )
            );
          } //TODO: make these tools not dependant on world
        } else if (selectedTile instanceof GroundTile) {
          if (toolEvent.getHoldableUsed().getName().equals("Hoe")) {
            ((GroundTile)selectedTile).setTilledStatus(true);
          } else if (toolEvent.getHoldableUsed().getName().equals("Pickaxe")) {
            ((GroundTile)selectedTile).setTilledStatus(false);
          } else if (toolEvent.getHoldableUsed().getName().equals("WateringCan") && 
                      (((GroundTile)selectedTile).getTilledStatus() == true)) {
            ((GroundTile)selectedTile).setLastWatered(this.inGameDay);
          }

          ((GroundTile)selectedTile).determineImage(this.inGameDay);
        }
      } else if (event instanceof UtilityUsedEvent) {
        //TODO: make this not just for forageables but also doors and stuff i guess
        Tile currentTile = this.playerArea.getMapAt(((UtilityUsedEvent) event).getLocationUsed());
        if (this.playerArea instanceof WorldArea) {
          ((WorldArea)this.playerArea).forageables.remove(currentTile);
        }
        //TODO: play foraging animation?
        TileComponent currentContent = currentTile.getContent();
        if (currentContent instanceof Collectable) {
        //TODO: make sure that when you create a new UtilityUsedEvent you check collectable
          HoldableDrop[] currentProducts = ((Collectable)currentContent).getProducts();
          // also for some reason the above is sometimes null and i don't know why :D
          HoldableStack drop = (currentProducts[0].resolveDrop(this.luckOfTheDay));
          new HoldableStackEntity(drop, null); // TODO: change the pos
          this.player.pickUp(drop); // does this not work or something? bc it doesn't draw hmmm
          currentTile.setContent(null);
        }
      } else if (event instanceof ComponentPlacedEvent) {
        Tile currentTile = this.playerArea.getMapAt(
                                        ((ComponentPlacedEvent)event).getLocationUsed());
        TileComponent currentContent = currentTile.getContent();
        if (currentContent == null) { //- Anything that you can place must not be placed over something
          //- We need to make sure that the tile is both a ground tile and is tilled if
          //- we're trying to plant a crop in that tile
          if (((ComponentPlacedEvent)event).getComponentToPlace() instanceof ExtrinsicCrop) {
            if (currentTile instanceof GroundTile) {
              if (((GroundTile)currentTile).getTilledStatus()) {
                currentTile.setContent(((ComponentPlacedEvent)event).getComponentToPlace());
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
    this.inGameNanoTime = (long)6*60*1_000_000_000;
    ++this.inGameDay;
    this.luckOfTheDay = Math.random();
    Iterator<Area> areas = this.locations.values().iterator();
    while (areas.hasNext()) {
      areas.next().doDayEndActions();
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

    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/Areas"));
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

    input = new BufferedReader(new FileReader("assets/gamedata/Connections"));
    nextLine = input.readLine();
    while (nextLine != null) {
      areaInfo = input.readLine().split(" ");
      for (int i = 0; i < 4; ++i) {
        if (!areaInfo[i].equals("null")) {
          this.locations.get(nextLine)
              .getNeighbourZone(i)
              .setDestinationArea(this.locations.get(areaInfo[i]), i);
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

  public static GatewayZone findNeighbourZone(Area a,
                                              int x, int y,
                                              boolean isHorizontal) {
    if (isHorizontal) {
      while (x < a.getWidth()-1 && a.getMapAt(x, y) == null) {
        ++x;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, isHorizontal);
      }
    } else {
      while (y < a.getHeight()-1 && a.getMapAt(x, y) == null) {
        ++y;
      }
      if (a.getMapAt(x, y) != null) {
        return new GatewayZone(x, y, isHorizontal);
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

  public long getInGameNanoTime() {
    return this.inGameNanoTime;
  }

  public long getInGameDay() {
    return this.inGameDay;
  }
}