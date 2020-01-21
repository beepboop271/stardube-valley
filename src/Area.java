import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [Area]
 * 2020-01-19
 * @version 1.21
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */

public abstract class Area {
  private final int WIDTH, HEIGHT;
  private String name;
  private Tile[][] map;
  private Set<Moveable> moveables;
  private Set<HoldableStackEntity> itemsOnGround;
  private GatewayZone[] neighbourZones;
  private LinkedHashMap<Point, Gateway> gateways;
  private long currentDay;
  private int currentSeason;

  /**
   * [Area]
   * Constructor for a new Area.
   * @author Kevin Qiao, Paula Yuan, Joseph Wang
   * @param name   The name of the area.
   * @param width  The width of the area, in tiles.
   * @param height The height of the area, in tiles.
   */
  public Area(String name, int width, int height) {
    this.name = name;
    this.WIDTH = width;
    this.HEIGHT = height;
    this.map = new Tile[this.HEIGHT][this.WIDTH];
    this.moveables = ConcurrentHashMap.newKeySet();
    this.itemsOnGround = ConcurrentHashMap.newKeySet();
    this.neighbourZones = new GatewayZone[4];
    this.gateways = new LinkedHashMap<Point, Gateway>();
    this.currentDay = 0;
    this.currentSeason = 0;
  }

  /**
   * [constructArea]
   * Constructs and returns a new area.
   * @author Kevin Qiao, Joseph Wang
   * @param category The type of the area.
   * @param name     The name of the area.
   * @param width    The width of the area.
   * @param height   The height of the area.
   * @return Area, the contructed area.
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
   * Returns the direction from a point to another point,
   * assuming both have at least one common coordinate (no diagonals).
   * @author Kevin Qiao
   * @param p1 The first point.
   * @param p2 The second point.
   * @return int, the direction from point 1 to point 2.
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
   * Determines what direction the player is colliding in,
   * if any. When the direction is unclear, only horizontal
   * or only vertical results can be specified by horizontalOnly.
   * @author Kevin Qiao
   * @param intersectingPoints The set of tile coordinates
   *                           the player is currently on top of.
   * @param pos                The coordinates of the player.
   * @param horizontalOnly     Whether or not to use horizontal or vertical
   *                           results when the direction is unclear.
   */
  public int collides(LinkedHashSet<Point> intersectingPoints, Point pos,
                      boolean horizontalOnly) {
    Point nextPoint;
    pos = pos.round();
    if (intersectingPoints.size() == 1) {
      // it is already on this tile, and on no other tiles
      // so it is not colliding
      return -1;
    } else if (intersectingPoints.size() == 2) {
      // get the direction from player to other point,
      // if the other position is not walkable
      intersectingPoints.remove(pos);
      nextPoint = intersectingPoints.iterator().next();
      if (!this.walkableAt(nextPoint)) {
        return Area.getDirection(pos, nextPoint);
      } else {
        return -1;
      }
    } else if (intersectingPoints.size() == 4) {
      // determine which tiles are not walkable around
      // the player, then based on horizontalOnly return
      // a result
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

  /**
   * [walkableAt]
   * Given a point, returns whether a movable can walk on it or not
   * @author Kevin Qiao, Paula Yuam, Joseph Wang
   * @param pos The point to be considered.
   * @return boolean, true if the point can be walked on, false otherwise.
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
        return false;
      }

      if (t.getContent() != null) {
        if (!(t.getContent() instanceof ExtrinsicTree)
              && (t.getContent() instanceof NotWalkable)) {
          return false;
        } else if ((t.getContent() instanceof ExtrinsicGrowableCollectable)
                   && !(t.getContent() instanceof ExtrinsicCrop)) {
          return false;
        }
      } else if ((bushTile != null) && (bushTile.getContent() != null)) {
        if ((bushTile.getContent() instanceof ExtrinsicGrowableCollectable)
              && !(bushTile.getContent() instanceof ExtrinsicCrop)) {
          return false;
        }
      }

      if (t instanceof NotWalkable) {
        return false;
      }

      if (this.inMap(treeX, treeY) && (this.getMapAt(treeX, treeY) != null)) {
        t = this.getMapAt(treeX, treeY);
        if ((t.getContent() != null) && (t.getContent() instanceof ExtrinsicTree)) {
          if (((ExtrinsicTree)t.getContent()).getStage() > 2) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * [moveAreas]
   * Determines whether or not the Moveable is walking over
   * any Gateways or GatewayZones. If so, the Moveable is
   * transported and each Area's records are updated.
   * @author Kevin Qiao
   * @param m                  The moveable to be moved.
   * @param intersectingPoints LinkedHashSet<Point>, the set of tile coordinates
   *                           the player is currently on top of.
   * @return Area, the Area which the player is now in after updating.
   */
  public Area moveAreas(Moveable m, Iterator<Point> intersectingPoints) {
    Point nextPoint;
    Gateway g;
    while (intersectingPoints.hasNext()) {
      nextPoint = intersectingPoints.next();
      if (!this.inMap(nextPoint)) {
        // moving out of map: GatewayZone to new Area
        g = this.getNeighbourZone(this.getExitDirection(nextPoint));
        if ((g != null) && !g.requiresInteractToMove()) {
          return this.moveAreas(m, g);
        }
      }
    }
    // or: single Gateway to another Area
    g = this.gateways.get(m.getPos().round());
    if (g != null && !g.requiresInteractToMove() && g.canMove(m)) {
      return this.moveAreas(m, g);
    }
    return this;
  }

  /**
   * [moveAreas]
   * Moves a Moveable into a different Area by calculating the
   * correct translated position and updating the records of
   * the old and new Area, returning the new Area.
   * @author Kevin Qiao
   * @param m           The moveable to be moved.
   * @param destination The destination gateway.
   * @return Area, the Area which the player is now in after updating.
   */
  public Area moveAreas(Moveable m, Gateway destination) {
    if (destination == null) {
      return this;
    }
    m.setPos(destination.toDestinationPoint(m.getPos(), m.getSize()));
    this.moveables.remove(m);
    destination.getDestinationArea().moveables.add(m);
    return destination.getDestinationArea();
  }

  /**
   * [moveAreas]
   * Takes the specified moveable and removes it from this area's list
   * of moveables, then adds it to the destination area's list of
   * moveables. Sets position in the other area to the specific position.
   * @author Joseph Wang
   * @param m           The moveable to be moved to a specific point.
   * @param position    The position of spawn inside the destination.
   * @param destination The area to go to.
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
   * Retrieves the neighbor zone of this area using the specified direction.
   * @param direction The direction of which to get the neighbor zone with.
   * @return GatewayZone, the neighbor zone of this area in the specified direction.
   */
  public GatewayZone getNeighbourZone(int direction) {
    return this.neighbourZones[direction];
  }

  /**
   * [setNeighbourZone]
   * Sets one of this area's neighbor zones to a specified GatewayZone
   * @param direction The direction to set.
   * @param g         The GatwayZone to set to.
   */
  public void setNeighbourZone(int direction, GatewayZone g) {
    this.neighbourZones[direction] = g;
  }

  /**
   * [addGateway]
   * Adds a gateway to this area's collection of gateways.
   * @param g The gateway to be added
   */
  public void addGateway(Gateway g) {
    this.gateways.put(g.getOrigin(), g);
  }

  /**
   * [getGateway]
   * Gets a gateway based on a given point.
   * @param pos The point which to get a gateway with.
   * @return Gateway, the gateway at the point provided,
   *         or null if none is present.
   */
  public Gateway getGateway(Point pos) {
    return this.gateways.get(pos);
  }

  /**
   * [getGateways]
   * Retrieves an iterator of the gateways in this area.
   * @return Iterator<Gateway>, the iterator of the gateways.
   */
  public Iterator<Gateway> getGateways() {
    return this.gateways.values().iterator();
  }

  /**
   * [getMoveables]
   * Retrieves an iterator of the moveables in this area.
   * @return Iterator<Moveable>, the iterator of the moveables.
   */
  public Iterator<Moveable> getMoveables() {
    return this.moveables.iterator();
  }

  /**
   * [getMoveableList]
   * Retrieves a new ArrayList of the moveables in this area.
   * @return ArrayList<Moveable>, the list of moveables.
   */
  public ArrayList<Moveable> getMoveableList() {
    return new ArrayList<Moveable>(this.moveables);
  }

  /**
   * [addMoveable]
   * Adds the moveable to this area.
   * @param m The Moveable to add.
   */
  public void addMoveable(Moveable m) {
    this.moveables.add(m);
  }

  /**
   * [addHarvestableAt]
   * Adds a harvestable at the given location in this area.
   * @param x           The x position of the destinated location.
   * @param y           The y position of the destinated location.
   * @param harvestable The name of the harvestable to add.
   */
  public void addHarvestableAt(int x, int y, String harvestable) {
    this.getMapAt(x, y).setContent(new ExtrinsicHarvestableComponent(harvestable));
  }

  /**
   * [removeComponentAt]
   * Removes the component at the given location in this area.
   * @param pos Point, the tile coordinates.
   */
  public void removeComponentAt(Point pos) {
    this.getMapAt(pos).setContent(null);
  }

  /**
   * [getItemsOnGround]
   * Retrieves an iterator of the items on ground in this area.
   * @return Iterator<HoldableStackEntity>, an iterator of the items on ground in this area.
   */
  public Iterator<HoldableStackEntity> getItemsOnGround() {
    return this.itemsOnGround.iterator();
  }

  /**
   * [getMoveableList]
   * Retrieves a new ArrayList of the items on the ground in this area.
   * @return ArrayList<Moveable>, the list of items on the ground.
   */
  public ArrayList<HoldableStackEntity> getItemsOnGroundList() {
    return new ArrayList<HoldableStackEntity>(this.itemsOnGround);
  }

  /**
   * [addItemOnGround]
   * Adds an item on ground in this area.
   * @param entity HoldableStackEntity, the item to add.
   */
  public void addItemOnGround(HoldableStackEntity entity) {
    this.itemsOnGround.add(entity);
  }

  /**
   * [getName]
   * Retrieves the name of this area.
   * @return String, the name of this area.
   */
  public String getName() {
    return this.name;
  }

  /**
   * [inMap]
   * Checks if the given location is within the map of the area.
   * @author Kevin Qiao
   * @param x The x position to check.
   * @param y The y position to check.
   * @return boolean, true if the position is within the area map,
   *         false otherwise.
   */
  public boolean inMap(int x, int y) {
    return (x >= 0) && (x < this.WIDTH) && (y >= 0) && (y < this.HEIGHT);
  }

  /**
   * [inMap]
   * Checks if the given location is within the map of the area.
   * @author Kevin Qiao
   * @param pos Point, the point to check.
   * @return boolean, true if the position is within the area map,
   *         false otherwise.
   */
  public boolean inMap(Point pos) {
    return this.inMap((int)pos.x, (int)pos.y);
  }

  /**
   * [getExitDirection]
   * Retrieves which direction a point is out of the area's
   * boundaries, if at all.
   * @author Kevin Qiao
   * @param pos The point to check.
   * @return int, the exit direction.
   */
  public int getExitDirection(Point pos) {
    int x = (int)(pos.x);
    int y = (int)(pos.y);
    if (x < 0) {
      return World.WEST;
    } else if (x >= this.WIDTH) {
      return World.EAST;
    } else if (y < 0) {
      return World.NORTH;
    } else if (y >= this.HEIGHT) {
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
    return this.WIDTH;
  }

  /**
   * [getHeight]
   * Retrieves the height of this area, in tiles.
   * @return int, the height of this area.
   */
  public int getHeight() {
    return this.HEIGHT;
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
   * @param x The x position in map.
   * @param y The y position in map.
   * @return Tile, the tile at the given position in map.
   */
  public Tile getMapAt(int x, int y) {
    return this.map[y][x];
  }

  /**
   * [getMapAt]
   * Retrieves the tile at the given position in map.
   * @param pos The position in map.
   * @return Tile, the tile at the given position in map.
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
   * Increases the day stored in this area by one.
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
    if ((this.currentDay%World.getDaysPerSeason() == 1) && (this.currentDay != 1)) {
      this.currentSeason = (this.currentSeason+1) % 4;
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
    // not abstract empty method as some areas do nothing
  }
}