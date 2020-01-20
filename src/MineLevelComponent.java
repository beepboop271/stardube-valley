import java.util.ArrayList;

/**
 * [MineLevelComponent]
 * A component of the mine level
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class MineLevelComponent {
  private final int WIDTH, HEIGHT;
  private boolean[][] map;
  private int possiblePaths;
  private ArrayList<ArrayList<Point>> paths;
  private int[] numPathsByDirection = new int[4];

  /**
   * [MineLevelComponent]
   * Constructor for a new MineLevelComponent.
   * @param width, the int width of the component
   * @param height, the int height of the component
   */
  public MineLevelComponent(int width, int height) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.possiblePaths = 0;
    this.map = new boolean[this.HEIGHT][this.WIDTH];
    this.paths = new ArrayList<ArrayList<Point>>(4);
    for (int i = 0; i < 4; ++i) {
      this.paths.add(new ArrayList<Point>(4));
    }
  }

  /**
   * [addPath]
   * Retrieves this player's inventory.
   * @param direction, int direction
   * @param pos, Point position
   */
  public void addPath(int direction, Point pos) {
    this.possiblePaths |= (1<<(direction));
    this.paths.get(direction).add(pos);
    ++this.numPathsByDirection[direction];
  }

  /**
   * [getPossiblePaths]
   * Retrieves the possible paths
   * @return the Int possiblePaths
   */
  public int getPossiblePaths() {
    return this.possiblePaths;
  }

  /**
   * [isWalkableAt]
   * Retrieves true or false value of whether a location is walkable.
   * @param x, int x position
   * @param y, int y position
   * @return boolean true or false
   */
  public boolean isWalkableAt(int x, int y) {
    return this.map[y][x];
  }

  /**
   * [setWalkableAt]
   * Set a location to walkable.
   * @param x, int x
   * @param y, int y
   */
  public void setWalkableAt(int x, int y) {
    this.map[y][x] = true;
  }

  /**
   * [getWidth]
   * Retrieves the width of the component
   * @return int constant width
   */
  public int getWidth() {
    return this.WIDTH;
  }

  /**
   * [getHeight]
   * Retrieves the height of the component
   * @return int constant height
   */
  public int getHeight() {
    return this.HEIGHT;
  }

  /**
   * [getRandomPathInDirection]
   * Retrieves a random path in a given direction
   * @param direction, int direction
   * @return Point path
   */
  public Point getRandomPathInDirection(int direction) {
    ArrayList<Point> paths = this.paths.get(direction);
    return paths.get((int)(Math.random()*paths.size()));
  }

  /**
   * [getRandomCommonPath]
   * Retrieves a random common path
   * @param otherPossiblePaths, int
   * @return an int path
   */
  public int getRandomCommonPath(int otherPossiblePaths) {
    int commonPaths = 0;
    if (((this.possiblePaths & (1<<World.NORTH)) > 0)
          && ((otherPossiblePaths & (1<<World.SOUTH)) > 0)) {
      commonPaths |= (1<<World.NORTH);
    }
    if (((this.possiblePaths & (1<<World.EAST)) > 0)
          && ((otherPossiblePaths & (1<<World.WEST)) > 0)) {
      commonPaths |= (1<<World.EAST);
    }
    if (((this.possiblePaths & (1<<World.SOUTH)) > 0)
          && ((otherPossiblePaths & (1<<World.NORTH)) > 0)) {
      commonPaths |= (1<<World.SOUTH);
    }
    if (((this.possiblePaths & (1<<World.WEST)) > 0)
          && ((otherPossiblePaths & (1<<World.EAST)) > 0)) {
      commonPaths |= (1<<World.WEST);
    }

    if (commonPaths == 0) {
      return -1;
    } else {
      int path;
      do {
        path = (int)(Math.random()*4);
      } while ((commonPaths & (1<<path)) == 0);
      return path;
    }
  }
}