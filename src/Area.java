import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * [Area]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public abstract class Area {
  private String name;
  private Tile[][] map;
  private LinkedHashSet<Moveable> moveables;
  private LinkedList<HoldableStackEntity> itemsOnGround;
  private final int width, height;
  private GatewayZone[] neighbourZone = new GatewayZone[4];
  
  public Area(String name,
              int width, int height) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = new Tile[this.height][this.width];
    this.moveables = new LinkedHashSet<Moveable>();
    this.itemsOnGround = new LinkedList<HoldableStackEntity>();
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

  public boolean collides(Iterator<Point> intersectingPoints) {
    Point nextPoint;
    while (intersectingPoints.hasNext()) {
      nextPoint = intersectingPoints.next();
      if (this.inMap((int)nextPoint.x, (int)nextPoint.y)
            && this.getMapAt((int)nextPoint.x, (int)nextPoint.y) == null) {
        return true;
      }
    }
    return false;
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
    m.setPos(gateway.toDestinationPoint(m.getPos()));
    this.moveables.remove(m);
    gateway.getDestinationArea().moveables.add(m);
    return gateway.getDestinationArea();
  }

  public GatewayZone getNeighbourZone(int i) {
    return this.neighbourZone[i];
  }

  public void setNeighbourZone(int i, GatewayZone g) {
    this.neighbourZone[i] = g;
  }

  public Iterator<Moveable> getMoveables() {
    return this.moveables.iterator();
  }

  public void addMoveable(Moveable m) {
    this.moveables.add(m);
  }

  public void removeComponentAt(Point pos) {
    this.getMapAt(pos).setContent(null);
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

  public Tile getMapAt(int x, int y) {
    return this.map[y][x];
  }

  public Tile getMapAt(Point pos) {
    return this.map[(int)pos.y][(int)pos.x];
  }

  public void setMapAt(Tile t) {
    this.map[t.getY()][t.getX()] = t;
  }

  public void doDayEndActions() {
  }
}