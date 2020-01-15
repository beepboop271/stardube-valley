import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * [Area]
 * 2019-12-19
 * @version 0.2
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */
public abstract class Area {
  private String name;
  private Tile[][] map;
  private LinkedHashSet<Moveable> moveables;
  private LinkedList<HoldableStackEntity> itemsOnGround;
  private final int width, height;
  private GatewayZone[] neighbourZones;
  private HashMap<Point, Gateway> gateways;
  private long currentDay;
  private int currentSeason;
  
  public Area(String name,
              int width, int height) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = new Tile[this.height][this.width];
    this.moveables = new LinkedHashSet<Moveable>();
    this.itemsOnGround = new LinkedList<HoldableStackEntity>();
    this.neighbourZones = new GatewayZone[4];
    this.gateways = new HashMap<Point, Gateway>();
    this.currentDay = 0;
    this.currentSeason = 0;
  }

  public static Area constructArea(String category,
                                   String name,
                                   int width, int height) {
    if (category.equals("FarmArea")) {
      return new FarmArea(name, width, height);
    } else if (category.equals("WorldArea")) {
      return new WorldArea(name, width, height);
    } else {
      return null;
    }
  }

  public static int getDirection(Point p1, Point p2) {
    // direction from p1 to p2
    if (p2.x-p1.x > 0) {
      return World.EAST;
    } else if (p2.x-p1.x < 0) {
      return World.WEST;
    } else if (p2.y-p1.y > 0) {
      return World.SOUTH;
    } else if (p2.y-p1.y < 0) {
      return World.NORTH;
    } else {
      return -1;
    }
  }

  public int collides(LinkedHashSet<Point> intersectingPoints, Point pos,
                      boolean horizontalOnly) {
    Point nextPoint;
    pos = pos.round();
    if (intersectingPoints.size() == 1) {
      return -1;
    } else if (intersectingPoints.size() == 2) {
      if (!intersectingPoints.remove(pos)) {
        throw new RuntimeException();
      }
      nextPoint = intersectingPoints.iterator().next();
      if (!this.walkableAt(nextPoint)) {
        return Area.getDirection(pos, nextPoint);
      } else {
        return -1;
      }
    } else if (intersectingPoints.size() == 4) {
      Point[] intersections = new Point[4];
      intersectingPoints.toArray(intersections);
      int collisions = 0;
      for (int i = 0; i < 4; ++i) {
        if (!this.walkableAt(intersections[i])) {
          collisions |= (1<<i);
        }
      }
      if (collisions == 0) {
        return -1;
      }
      if (horizontalOnly) {
        if (((collisions & (1<<1)) > 0)
              || ((collisions & (1<<2)) > 0)) {
          return World.EAST;
        } else {
          return World.WEST;
        }
      } else {
        if (((collisions & (1<<0)) > 0)
              || ((collisions & (1<<1)) > 0)) {
          return World.NORTH;
        } else {
          return World.SOUTH;
        }

      }
    }
    return -1;
  }

  public boolean walkableAt(Point pos) {
    if (this.inMap(pos)) {
      int treeX = (int)pos.x+2;     // TODO: make this less... sketchy, I guess
      int treeY = (int)pos.y+1;

      Tile t = this.getMapAt(pos);
      if (t == null) {
        return false;
      }

      if (t.getContent() != null) {
        if (t.getContent() instanceof CollectableComponent) {// ExtrinsicHarvestableComponent) {
          return false;
        }
      }

      if (t instanceof WaterTile) {
        return false;
      }

      if (this.inMap(treeX, treeY) && this.getMapAt(treeX, treeY) != null) {
        t = this.getMapAt(treeX, treeY);
        if (t.getContent() != null && t.getContent() instanceof ExtrinsicTree) {
          return false;
        }
      }

    }
    return true;
  }

  public boolean hasLineOfSight(Enemy e, Player p) {
    Point p1 = e.getPos();
    Point p2 = p.getPos();
    Vector2D step = new Vector2D(p2.x-p1.x, p2.y-p1.y).setLength(0.5);
    double dist = p1.distanceTo(p2);
    for (int i = 0; i < dist/0.5; ++i) {
      p1.translate(step.getX(), step.getY());
      switch (e.getHeight()) {
        case 1:
          if (!this.walkableAt(p1.round())) {
            return false;
          }
          break;
        case 2:
          if (!this.inMap(p1.round()) || (this.getMapAt(p1.round()) == null)) {
            return false;
          }
          break;
        case 3:
          return true;
      }
    }
    return true;
  }

  public int canMoveAreas(Iterator<Point> intersectingPoints) {
    Point nextPoint;
    while (intersectingPoints.hasNext()) {
      nextPoint = intersectingPoints.next();
      if (!this.inMap((int)nextPoint.x, (int)nextPoint.y)) {
        return this.getExitDirection((int)nextPoint.x, (int)nextPoint.y);
      }
    }
    return -1;
  }

  public Area moveAreas(Moveable m, int direction) {
    GatewayZone gateway = this.getNeighbourZone(direction);
    return this.moveAreas(m, gateway);
  }

  public Area moveAreas(Moveable m, Gateway g) {
    m.setPos(g.toDestinationPoint(m.getPos(), m.getSize()));
    this.moveables.remove(m);
    g.getDestinationArea().moveables.add(m);
    return g.getDestinationArea();
  }

  public GatewayZone getNeighbourZone(int i) {
    return this.neighbourZones[i];
  }

  public void setNeighbourZone(int i, GatewayZone g) {
    this.neighbourZones[i] = g;
  }

  public void addGateway(Gateway g) {
    this.gateways.put(g.getOrigin(), g);
  }

  public Gateway getGateway(Point p) {
    return this.gateways.get(p);
  }

  public Iterator<Moveable> getMoveables() {
    return this.moveables.iterator();
  }

  public void addMoveable(Moveable m) {
    this.moveables.add(m);
  }

  public void removeMoveable(Moveable m) {
    //testing only TODO: remove
    this.moveables.remove(m);
  }

  public void addHarvestableAt(int x, int y, String harvestable) {
    this.getMapAt(x, y).setContent(new ExtrinsicHarvestableComponent(harvestable));
  }

  public void removeComponentAt(Point pos) {
    this.getMapAt(pos).setContent(null);
  }

  public LinkedList<HoldableStackEntity> getItemsOnGroundList() {
    return this.itemsOnGround;
  }

  public Iterator<HoldableStackEntity> getItemsOnGround() {
    return this.itemsOnGround.iterator();
  }

  public void addItemOnGround(HoldableStackEntity e) {
    this.itemsOnGround.addLast(e);
  }

  public String getName() {
    return this.name;
  }

  public boolean inMap(int x, int y) {
    return x >= 0 && x < this.width && y >= 0 && y < this.height;
  }

  public boolean inMap(Point pos) {
    return this.inMap((int)pos.x, (int)pos.y);
  }

  public int getExitDirection(int x, int y) {
    if (x < 0) {
      return World.WEST;
    } else if (x >= this.width) {
      return World.EAST;
    } else if (y < 0) {
      return World.NORTH;
    } else if (y >= this.height) {
      return World.SOUTH;
    } else {
      return -1;
    }
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public Tile[][] getMap() {
    return this.map;
  }
  
  public Tile getMapAt(int x, int y) {
    return this.map[y][x];
  }

  public Tile getMapAt(Point pos) {
    return this.map[(int)pos.y][(int)pos.x];
  }

  public void setMapAt(Tile t) {
    this.map[t.getY()][t.getX()] = t;
  }

  public void updateDay() {
    this.currentDay += 1;
  }

  public long getCurrentDay() {
    return this.currentDay;
  }

  public void updateSeason() {
    if ((this.currentDay % World.getDaysPerSeason() == 1) && (this.currentDay != 1)) {
      this.currentSeason = (this.currentSeason + 1) % 3;
    }
  }

  public int getSeason() {
    return this.currentSeason;
  }

  public boolean hasValidXYAt(int x, int y) {
    return (((y >= 0) && (y < this.height) && (x >= 0) && (x < this.width)));
  }
  
  public boolean hasValidPointAt(Point pos) {
    return ((((int)pos.y >= 0) && ((int)pos.y < this.height) && ((int)pos.x >= 0) && ((int)pos.x < this.width)));
  }
  
  public void doDayEndActions() {
  }
}