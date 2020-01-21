import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * [Area]
 * 2019-12-19
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */

public abstract class Area {
  private String name;
  private Tile[][] map;
  // private List<Moveable> moveables;
  // private List<HoldableStackEntity> itemsOnGround;
  // private SortedSet<Moveable> moveables;                 //
  // private SortedSet<HoldableStackEntity> itemsOnGround;  //
  // private ConcurrentLinkedQueue<Moveable> moveableQueue;
  // private ConcurrentLinkedQueue<HoldableStackEntity> itemsOnGroundQueue;
  private Set<Moveable> moveables;
  private Set<HoldableStackEntity> itemsOnGround;
  private final int width, height;
  private GatewayZone[] neighbourZones;
  private LinkedHashMap<Point, Gateway> gateways;
  private long currentDay;
  private int currentSeason;

  /**
   * [Area]
   * Constructor for a new Area.
   * @author         Kevin Qiao, Paula Yuan, Joseph Wang
   * @param name     String, The name of the area.
   * @param width    int, The width of the area, in tiles.
   * @param height   int, The height of the area, in tiles.
   */
  public Area(String name, int width, int height) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = new Tile[this.height][this.width];
    this.moveables = ConcurrentHashMap.newKeySet();
    this.itemsOnGround = ConcurrentHashMap.newKeySet();
    // this.moveables = Collections.synchronizedList(new ArrayList<Moveable>());
    // this.itemsOnGround = Collections.synchronizedList(new ArrayList<HoldableStackEntity>());
    // this.moveables = Collections.synchronizedSortedSet(new TreeSet<Moveable>());
    // this.itemsOnGround = Collections.synchronizedSortedSet(new TreeSet<HoldableStackEntity>());
    this.neighbourZones = new GatewayZone[4];
    this.gateways = new LinkedHashMap<Point, Gateway>();
    this.currentDay = 0;
    this.currentSeason = 0;
  }

  /**
   * [constructArea]
   * Constructs and retrieves a new area.
   * @author          Kevin Qiao, Joseph Wang
   * @param category  String, the category of the area.
   * @param name      String, the name of the area.
   * @param width     Int, the width of the area.
   * @param height    Int, the height of the area.
   * @return          Area, the contructed area.
   */
  public static Area constructArea(String category,
                                   String name,
                                   int width, int height) {
    if (category.equals("FarmArea")) {
      return new FarmArea(name, width, height);
    } else if (category.equals("WorldArea")) {
      return new WorldArea(name, width, height);
    } else if (category.equals("BuildingArea")) {
      return new BuildingArea(name, width, height);
    } else if (category.equals("SpawnBuildingArea")) {
      return new SpawnBuildingArea(name, width, height);
    } else if (category.equals("TownArea")) {
      return new TownArea(name, width, height);
    }

    return null;
  }

  /**
   * [getDirection]
   * Retrieves the direction from a point to another point.
   * @author        Kevin Qiao
   * @param p1      Point, the first point.
   * @param p2      Point, the second point.
   * @return        int, the direction from point 1 to point 2.
   */
  public static int getDirection(Point p1, Point p2) {
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

  /**
   * [collides]
   * @author Kevin Qiao
   * @param intersectingPoints
   * @param pos
   * @param horizontalOnly
   */
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
        System.out.print(nextPoint+" ");
        // System.out.println(this.walkableAt(nextPoint));
        return Area.getDirection(pos, nextPoint);
      } else {
        return -1;
      }
    } else if (intersectingPoints.size() == 4) {
      System.out.println("help me aaaaaaaaaaaa");
      Point[] intersections = new Point[4];
      intersectingPoints.toArray(intersections);
      int collisions = 0;
      for (int i = 0; i < 4; ++i) {
        if (!this.walkableAt(intersections[i])) {
          collisions |= (1<<i);
        }
      }
      System.out.println(Integer.toBinaryString(collisions));
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

  /**
   * [walkableAt]
   * Given a point, calculates whether a movable can walk on it or not
   * @author    Kevin Qiao, Paula Yuam, Joseph Wang
   * @param pos The point to be considered.
   * @return    boolean, true if the point can be walked on, false otherwise.
   */
  public boolean walkableAt(Point pos) {
    if (this.inMap(pos)) {
      int treeX = (int)pos.x+2;
      int treeY = (int)pos.y+1;
      
      Tile bushTile = null;
      if (this.inMap((int)pos.x+1, (int)pos.y)) {
        bushTile = this.getMapAt((int)pos.x+1, (int)pos.y);
      }

      Tile t = this.getMapAt(pos);
      if (t == null) {
        System.out.println("false: t null");
        return false;
      }

      if (t.getContent() != null) {
        if (!(t.getContent() instanceof ExtrinsicTree)
              && (t.getContent() instanceof NotWalkable)) {
          System.out.println("false: tc not walkable");
          return false;
        } else if (t.getContent() instanceof ExtrinsicGrowableCollectable && 
                  !(t.getContent() instanceof ExtrinsicCrop)) {
          System.out.println("false: tc bush?????");
          return false;
        }
      } else if (bushTile != null && bushTile.getContent() != null) {
         if (bushTile.getContent() instanceof ExtrinsicGrowableCollectable 
            && !(bushTile.getContent() instanceof ExtrinsicCrop)) {
           System.out.println("false: tc bush probably???");
           return false;
         }
      }

      if (t instanceof NotWalkable) {
        System.out.println("false: t not walkable "+t.getClass().getName());
        return false;
      }

      if (this.inMap(treeX, treeY) && this.getMapAt(treeX, treeY) != null) {
        t = this.getMapAt(treeX, treeY);
        if (t.getContent() != null && t.getContent() instanceof ExtrinsicTree) {
          if (((ExtrinsicTree)t.getContent()).getStage() > 2) {
            System.out.println("false: tree");
            return false;
          }
        }
      }

    }
    System.out.println("true: wow");
    return true;
  }

  /**
   * [hasLineOfSight]
   * @author unknown
   */
  public boolean hasLineOfSight(Enemy e, Player p) {
    if (e.getHeight() == 3) {
      // flying enemies always have line of sight
      return true;
    }
    Point p1 = e.getPos();
    Point p2 = p.getPos();
    Vector2D step = new Vector2D(p2.x-p1.x, p2.y-p1.y).setLength(0.3);
    double dist = p1.distanceTo(p2);
    for (int i = 0; i < dist/0.3; ++i) {
      p1.translate(step.getX(), step.getY());
      switch (e.getHeight()) {
        case 1:
          //System.out.println(p1);
          if (!this.walkableAt(p1.round())) {
            return false;
          }
          break;
        case 2:
          if (!this.inMap(p1.round()) || (this.getMapAt(p1.round()) == null)) {
            return false;
          }
          break;
      }
    }
    return true;
  }

  /**
   * [moveAreas]
   * Takes the specified moveable and removes it from this area's list of
   * moveables. Adds the moveable to the area associated to the provided
   * set of intersections and sets position accordingly to the gateway zone.
   * @author                    Kevin Qiao
   * @param m                   The moveable to be moved.
   * @param intersectingPoints  The destination gateway.
   * @return                    Area, the destination area.
   */
  public Area moveAreas(Moveable m, Iterator<Point> intersectingPoints) {
    Point nextPoint;
    Gateway g;
    while (intersectingPoints.hasNext()) {
      nextPoint = intersectingPoints.next();
      if (!this.inMap(nextPoint)) {
        g = this.getNeighbourZone(this.getExitDirection(nextPoint));
        if ((g != null) && !g.requiresInteractToMove()) {
          return this.moveAreas(m, g);
        }
      }
    }
    g = this.gateways.get(m.getPos().round());
    if (g != null && !g.requiresInteractToMove() && g.canMove(m)) {
      return this.moveAreas(m, g);
    }
    return this;
  }

  /**
   * [moveAreas]
   * Takes the specified moveable and removes it from this area's list of
   * moveables. Adds the moveable to the area associated to the provided
   * gateway and sets position accordingly to the gateway.
   * @author     Kevin Qiao
   * @param m    The moveable to be moved.
   * @param g    The destination gateway.
   * @return Area, the destination area.
   */
  public Area moveAreas(Moveable m, Gateway g) {
    if (g == null) {
      return this;
    }
    m.setPos(g.toDestinationPoint(m.getPos(), m.getSize()));
    this.moveables.remove(m);
    g.getDestinationArea().moveables.add(m);
    return g.getDestinationArea();
  }

  /**
   * [moveAreas]
   * Takes the specified moveable and removes it from this area's list
   * of moveables, then adds it to the destination area's list of
   * moveables. Sets position in the other area to the specific position.
   * @author Joseph Wang
   * @param m           the moveable to be moved to a specific point.
   * @param position    the position of spawn inside the destination.
   * @param destination the area to go to.a
   * @return Area, the destination area.
   */
  public Area moveAreas(Moveable m, Point position, Area destination) {
    m.setPos(position);
    this.moveables.remove(m);
    destination.moveables.add(m);

    return destination;
  }

  /**
   * [getNeighbourZone]
   * Retrieves the neighbor zone of this area using the specified index.
   * @author Kevin Qiao
   * @param i  The index of which to get the neighbor zone with.
   * @return   GatewayZone, the neighbor zone of this area.
   */
  public GatewayZone getNeighbourZone(int i) {
    return this.neighbourZones[i];
  }

  /**
   * [setNeighbourZone]
   * Sets one of this area's neighbor zone to a specified GatewayZone
   * @author Kevin Qiao
   * @param i  The index to set.
   * @param g  The GatwayZone to set to.
   */
  public void setNeighbourZone(int i, GatewayZone g) {
    this.neighbourZones[i] = g;
  }

  /**
   * [addGateway]
   * Adds a gateway to this area's collection of gateways. Used for
   * buildings.
   * @author Kevin Qiao
   * @param g  The gateway to be added
   */
  public void addGateway(Gateway g) {
    this.gateways.put(g.getOrigin(), g);
  }

  /**
   * [getGateway]
   * Gets a gateway based on a given point.
   * @author Kevin Qiao
   * @param pos  The point which to get a gateway with.
   * @return     Gateway, the gateway at the point
   */
  public Gateway getGateway(Point pos) {
    return this.gateways.get(pos);
  }

  /**
   * [getGateways]
   * Retrieves the iterator of the gateways in this area.
   * @return    Iterator<Gateway>, the iterator of the gateways.
   */
  public Iterator<Gateway> getGateways() {
    return this.gateways.values().iterator();
  }

  // public ArrayList<Moveable> getMoveableList() {
  //   return this.moveables;
  // }

  /**
   * [getMoveables]
   * Retrieves the iterator of the moveables in this area.
   * @return    Iterator<Moveable>, the iterator of the moveables.
   */
  public Iterator<Moveable> getMoveables() {
    return this.moveables.iterator();
    // return this.moveables.iterator();
  }

  public ArrayList<Moveable> getMoveableList() {
    return new ArrayList<Moveable>(this.moveables);
  }

  // public Moveable[] getMoveables(int row) {
  // public Iterator<Moveable> getMoveables(int row) {
  //   // moveable is abstract, any subclass will do
  //   // HoldableStackEntity is the only non animated Moveable
  //   Moveable lowerBound = new HoldableStackEntity(null, new Point(0, row));
  //   Moveable upperBound = new HoldableStackEntity(null, new Point(this.width, row+1));
  //   // subSet: elements in range [lowerBound, upperBound)
  //   // int startIdx = Collections.binarySearch(this.moveables, lowerBound);
  //   // if (startIdx < 0) {
  //   //   startIdx = -(startIdx+1);
  //   // }
  //   // int endIdx = Collections.binarySearch(this.moveables, upperBound);
  //   // if (endIdx < 0) {
  //   //   endIdx = -(endIdx+1);
  //   // }
  //   // return this.moveables.subList(startIdx, endIdx).iterator();//.toArray(new Moveable[0]);
  //   // synchronized (this.moveables) {
  //     return this.moveables.subSet(lowerBound, upperBound).toArray(new Moveable[0]);
  //   // }
  // }

  public void removeMoveable(Moveable m) {
    this.moveables.remove(m);
  }

  // public void sortMoveables() {
  //   synchronized (this.moveables) {
  //     Collections.sort(this.moveables);
  //   }
  // }

  /**
   * [addMoveable]
   * Adds the moveable to this area.
   * @author Kevin Qiao
   * @param m   The Moveable to add.
   */
  public void addMoveable(Moveable m) {
    this.moveables.add(m);
  }

  /**
   * [addHarvestableAt]
   * Adds a harvestable at the given location in this area.
   * @author Kevin Qiao
   * @param x             int, the x position of the destinated location.
   * @param y             int, the y position of the destinated location.
   * @param harvestable   String, the name of the harvestable to add.
   */
  public void addHarvestableAt(int x, int y, String harvestable) {
    this.getMapAt(x, y).setContent(new ExtrinsicHarvestableComponent(harvestable));
  }

  /**
   * [removeComponentAt]
   * Removes the component at the given location in this area.
   * @author Kevin Qiao
   * @param pos     Point, the destinated tile location.
   */
  public void removeComponentAt(Point pos) {
    this.getMapAt(pos).setContent(null);
  }

  // public ArrayList<HoldableStackEntity> getItemsOnGroundList() {
  //   return this.itemsOnGround;
  // }

  /**
   * [getItemsOnGround]
   * Retrieves the iterator of the items on ground in this area.
   * @return  Iterator<HoldableStackEntity>, an iterator of the items on ground in this area.
   */
  public Iterator<HoldableStackEntity> getItemsOnGround() {
    System.out.println(this.itemsOnGround.size());
    return this.itemsOnGround.iterator();
  }

  public ArrayList<HoldableStackEntity> getItemsOnGroundList() {
    return new ArrayList<HoldableStackEntity>(this.itemsOnGround);
  }

  // public HoldableStackEntity[] getItemsOnGround(int row) {
  // public Iterator<HoldableStackEntity> getItemsOnGround(int row) {
  //   // moveable is abstract, any subclass will do
  //   // HoldableStackEntity is the only non animated Moveable
  //   HoldableStackEntity lowerBound = new HoldableStackEntity(null, new Point(0, row));
  //   HoldableStackEntity upperBound = new HoldableStackEntity(null, new Point(this.width, row+1));
  //   // subSet: elements in range [lowerBound, upperBound)
  //   int startIdx = Collections.binarySearch(this.itemsOnGround, lowerBound);
  //   if (startIdx < 0) {
  //     startIdx = -(startIdx+1);
  //   }
  //   int endIdx = Collections.binarySearch(this.itemsOnGround, upperBound);
  //   if (endIdx < 0) {
  //     endIdx = -(endIdx+1);
  //   }
  //   return this.itemsOnGround.subList(startIdx, endIdx).iterator();//.toArray(new HoldableStackEntity[0]);
  //   // return this.itemsOnGround.subSet(lowerBound, upperBound).toArray(new HoldableStackEntity[0]);
  // }

  // public void sortItemsOnGround() {
  //   synchronized (this.itemsOnGround) {
  //     Collections.sort(this.itemsOnGround);
  //   }
  // }

  public void removeItemOnGround(HoldableStackEntity stack) {
    this.itemsOnGround.remove(stack);
  }

  /**
   * [addItemOnGround]
   * Adds an item on ground in this area.
   * @param e   HoldableStackEntity, the item to add.
   */
  public void addItemOnGround(HoldableStackEntity e) {
    this.itemsOnGround.add(e);
  }

  /**
   * [getName]
   * Retrieves the name of this area.
   * @return    String, the name of this area.
   */
  public String getName() {
    return this.name;
  }

  /**
   * [inMap]
   * Checks if the given location is within the map of the area.
   * @author    Kevin Qiao
   * @param x   int, the x position to check.
   * @param y   int, the y position to check.
   * @return    boolean, true if the position is within the area map,
   *            false otherwise.
   */
  public boolean inMap(int x, int y) {
    return x >= 0 && x < this.width && y >= 0 && y < this.height;
  }

  /**
   * [inMap]
   * Checks if the given location is within the map of the area.
   * @author      Kevin Qiao
   * @param pos   Point, the point to check.
   * @return      boolean, true if the position is within the area map,
   *              false otherwise.
   */
  public boolean inMap(Point pos) {
    return this.inMap((int)pos.x, (int)pos.y);
  }

  /**
   * [getExitDirection]
   * Retrieves the exit direction based on a point.
   * @author     Kevin Qiao
   * @param pos  The point to be used during direction calculation
   * @return     int, with the exit direction (as an int)
   */
  public int getExitDirection(Point pos) {
    int x = (int)(pos.x);
    int y = (int)(pos.y);
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

  /**
   * [getWidth]
   * Retrieves the width of this area, in tiles.
   * @return int, the width of this area.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * [getHeight]
   * Retrieves the height of this area, in tiles.
   * @return int, the height of this area.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * [getMap]
   * Retrieves the map of this area.
   * @return Tile[][], the map of this area.
   */
  public Tile[][] getMap() {
    return this.map;
  }
  
  /**
   * [getMapAt]
   * Retrieves the tile at the given position in map.
   * @param x   int, the x position in map.
   * @param y   int, the y position in map.
   * @return    Tile, the tile at the given position in map.
   */
  public Tile getMapAt(int x, int y) {
    return this.map[y][x];
  }

  /**
   * [getMapAt]
   * Retrieves the tile at the given position in map.
   * @param pos  Point, the posistion in map.
   * @return     Tile, the tile at the given position in map.
   */
  public Tile getMapAt(Point pos) {
    return this.map[(int)pos.y][(int)pos.x];
  }

  /**
   * [setMapAt]
   * Sets the given tile into the area map.
   * @param t  Tile, the tile to set.
   */
  public void setMapAt(Tile t) {
    this.map[t.getY()][t.getX()] = t;
  }

  /**
   * [updateDay]
   * Increases the day stored in this area by one, effectively updating it.
   */
  public void updateDay() {
    this.currentDay += 1;
  }

  /**
   * [getCurrentDay]
   * Retrieves the day stored in this area.
   * @return long, the day stored in this area.
   */
  public long getCurrentDay() {
    return this.currentDay;
  }

  /**
   * [updateSeason]
   * Updates the stored season in this area according to the day stored in this area.
   * @author Joseph Wang, Candice Zhang
   */
  public void updateSeason() {
    if ((this.currentDay % World.getDaysPerSeason() == 1) && (this.currentDay != 1)) {
      this.currentSeason = (this.currentSeason + 1) % 4;
    }
  }

  /**
   * [getSeason]
   * Retrieves the current season stored in this area.
   * @return int, the current season stored in this area.
   */
  public int getSeason() {
    return this.currentSeason;
  }
  
  /**
   * [doDayEndActions]
   * Does actions that need to be performed at the end of day.
   */
  public void doDayEndActions() {
  }
}