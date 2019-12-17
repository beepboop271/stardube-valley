import java.util.LinkedHashSet;

/**
 * [Area]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class Area {
  private String name;
  private Tile[][] map;
  private LinkedHashSet<Moveable> moveables;
  private LinkedHashSet<TileComponent> components;
  private final int width, height;

  public Area(String name,
              int width, int height,
              Area topNeighbour, Area rightNeighbour,
              Area bottomNeighbour, Area leftNeighbour) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = new Tile[this.height][this.width];
    this.components = new LinkedHashSet<TileComponent>();
    this.moveables = new LinkedHashSet<Moveable>();
  }

  public static Area constructArea(String category,
                                   String name, char id,
                                   int width, int height) {
    if (category.equals("FarmArea")) {
      return new FarmArea(name, id, width, height);
    } else if (category.equals("WorldArea")) {
      return new WorldArea(name, id, width, height);
    } else {
      return null;
    }
  }

  public char getID() {
    return this.id;
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