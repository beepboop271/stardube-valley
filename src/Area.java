import java.util.LinkedHashSet;

/**
 * [Area]
 * 2019-12-18
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class Area {
  private String name;
  private Tile[][] map;
  private LinkedHashSet<Moveable> moveables;
  private LinkedHashSet<TileComponent> components;
  private final int width, height;
  private GatewayZone[] neighbourZone = new GatewayZone[4];
  
  public Area(String name,
              int width, int height) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = new Tile[this.height][this.width];
    this.components = new LinkedHashSet<TileComponent>();
    this.moveables = new LinkedHashSet<Moveable>();
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

  public GatewayZone getNeighbourZone(int i) {
    return this.neighbourZone[i];
  }

  public void setNeighbourZone(int i, GatewayZone g) {
    this.neighbourZone[i] = g;
  }

  public String getName() {
    return this.name;
  }

  public boolean inMap(int x, int y) {
    return x >= 0 && x < this.width && y >= 0 && y < this.height;
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

  public void setMapAt(Tile t) {
    this.map[t.getY()][t.getX()] = t;
  }
}