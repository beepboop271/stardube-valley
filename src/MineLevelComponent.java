import java.util.ArrayList;

public class MineLevelComponent {
  private final int WIDTH, HEIGHT;
  private boolean[][] map;
  private int possiblePaths;
  private ArrayList<ArrayList<Point>> paths;
  private int[] numPathsByDirection = new int[4];

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

  public void addPath(int direction, Point pos) {
    this.possiblePaths |= (1<<(direction));
    this.paths.get(direction).add(pos);
    ++this.numPathsByDirection[direction];
  }

  public int getPossiblePaths() {
    return this.possiblePaths;
  }

  public boolean isWalkableAt(int x, int y) {
    return this.map[y][x];
  }

  public void setWalkableAt(int x, int y) {
    this.map[y][x] = true;
  }

  public int getWidth() {
    return this.WIDTH;
  }

  public int getHeight() {
    return this.HEIGHT;
  }

  public Point getRandomPathInDirection(int direction) {
    ArrayList<Point> paths = this.paths.get(direction);
    return paths.get((int)(Math.random()*paths.size()));
  }

  public int getRandomCommonPath(int otherPossiblePaths) {
    int commonPaths = 0;
    if (((this.possiblePaths & (1<<World.NORTH)) > 0)
          && ((otherPossiblePaths & (1<<World.SOUTH)) > 0)) {
      commonPaths |= (1<<World.NORTH);
    } else if (((this.possiblePaths & (1<<World.EAST)) > 0)
          && ((otherPossiblePaths & (1<<World.WEST)) > 0)) {
      commonPaths |= (1<<World.EAST);
    } else if (((this.possiblePaths & (1<<World.SOUTH)) > 0)
          && ((otherPossiblePaths & (1<<World.NORTH)) > 0)) {
      commonPaths |= (1<<World.SOUTH);
    } else if (((this.possiblePaths & (1<<World.WEST)) > 0)
          && ((otherPossiblePaths & (1<<World.EAST)) > 0)) {
      commonPaths |= (1<<World.WEST);
    }

    if (commonPaths == 0) {
      return -1;
    } else {
      int path;
      do {
        path = (int)(Math.random()*4);
      } while ((commonPaths & (1<<path)) == 1);
      return path;
    }
  }
}