import java.awt.event.KeyEvent;
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
 * @author Kevin Qiao, Paula Yuan
 */
public class World {
  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;

  private LinkedHashMap<String, Area> locations;
  private PriorityBlockingQueue<TimedEvent> eventQueue;
  private Area playerArea;
  private Player player;
  private long lastUpdateTime = System.nanoTime();
  private long inGameNanoTime;
  private long inGameDay = 0;

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

    // pygame_irl
    EventObject event;
    while (!this.eventQueue.isEmpty()
            && (this.eventQueue.peek().getTime() <= this.inGameNanoTime)) {
       event = this.eventQueue.poll().getEvent();
    }

    if (this.player.isInMenu()) {
      this.lastUpdateTime = currentUpdateTime;
      return;
    }

    this.inGameNanoTime += currentUpdateTime-this.lastUpdateTime;
    this.inGameNanoTime %= (long)24*60*1_000_000_000;

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
          // System.out.println(intersectingTiles.toString());
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

  public void doDayEndActions() {
    // day starts at 6 am
    this.inGameNanoTime = (long)6*60*1_000_000_000;
    ++this.inGameDay;
    Iterator<Area> areas = this.locations.values().iterator();
    while (areas.hasNext()) {
      areas.next().doDayEndActions();
    }
  }

  public void enqueueEvent(TimedEvent te) {
    this.eventQueue.offer(te);
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