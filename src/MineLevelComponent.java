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

  /**
   * [MineLevelComponent]
   * Constructor for a new MineLevelComponent.
   * @param width  The width of the component.
   * @param height The height of the component.
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
   * Adds a possible path out of this component and
   * records it as a possible direction.
   * @param direction The direction this path leads.
   * @param pos       The location of this path.
   */
  public void addPath(int direction, Point pos) {
    this.possiblePaths |= (1<<(direction));
    this.paths.get(direction).add(pos);
  }

  /**
   * [getPossiblePaths]
   * Retrieves the possible directions as an integer. Each
   * bit indicates one direction.
   * @return int, the bit flags of possible paths from this component.
   */
  public int getPossiblePaths() {
    return this.possiblePaths;
  }

  /**
   * [isWalkableAt]
   * Retrieves true or false value of whether a location is walkable.
   * @param x The x position to check.
   * @param y The y position to check.
   * @return boolean, true if the component is walkable at the position
   *         specified, false otherwise.
   */
  public boolean isWalkableAt(int x, int y) {
    return this.map[y][x];
  }

  /**
   * [setWalkableAt]
   * Sets a location as walkable.
   * @param x The x position to set.
   * @param y The y position to set.
   */
  public void setWalkableAt(int x, int y) {
    this.map[y][x] = true;
  }

  /**
   * [getWidth]
   * Retrieves the width of this component.
   * @return int, the width in tiles.
   */
  public int getWidth() {
    return this.WIDTH;
  }

  /**
   * [getHeight]
   * Retrieves the height of this component.
   * @return int, the height in tiles.
   */
  public int getHeight() {
    return this.HEIGHT;
  }

  /**
   * [getRandomPathInDirection]
   * Retrieves a random path location in a given direction.
   * @param direction The direction to get a path in.
   * @return Point, the position in this component the path
   *         selected is located.
   */
  public Point getRandomPathInDirection(int direction) {
    ArrayList<Point> paths = this.paths.get(direction);
    return paths.get((int)(Math.random()*paths.size()));
  }

  /**
   * [getRandomCommonPath]
   * Finds the common paths between two components, then
   * selects a random direction out of the common ones.
   * @param otherPossiblePaths The bit flags indicating possible paths from
   *                           the other MineLevelComponent.
   * @return int, a direction which this component can connect
   *         to another component in.
   */
  public int getRandomCommonPath(int otherPossiblePaths) {
    int commonPaths = 0;
    // if this component leads north, it will become
    // the south of the other component
    if (((this.possiblePaths & (1<<World.NORTH)) > 0)
          && ((otherPossiblePaths & (1<<World.SOUTH)) > 0)) {
      commonPaths |= (1<<World.NORTH);
    }
    // and etc..
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
      // no paths were found
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