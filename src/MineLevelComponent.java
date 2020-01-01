import java.util.ArrayList;
import java.util.Random;

public class MineLevelComponent {
  private final int WIDTH, HEIGHT;
  private boolean[][] map;
  private int possiblePaths;
  private ArrayList<Point> paths;
  private int[] numPathsByDirection = new int[4];

  public MineLevelComponent(int width, int height) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.possiblePaths = 0;
    this.map = new boolean[this.HEIGHT][this.WIDTH];
    this.paths = new ArrayList<Point>(4);
  }

  public void addPath(int direction, Point pos) {
    this.possiblePaths |= (1<<(direction));
    this.paths.add(pos);
    ++this.numPathsByDirection[direction];
  }

  public boolean hasPath(int direction) {
    return (this.possiblePaths & (1<<direction)) == 1;
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

  public ArrayList<Point> getPaths() {
    return this.paths;
  }

  public int[] getNumPathsByDirection() {
    return this.numPathsByDirection;
  }

  public int getRandomContainedPath(Random rand) {
    if (this.possiblePaths == 0) {
      throw new RuntimeException("no paths");
    } else {
      int path;
      do {
        path = rand.nextInt(4);
      } while (!this.hasPath(path));
      return path;
    }
  }

  public int getRandomCommonPath(Random rand, int otherPossiblePaths) {
    int commonPaths = (this.possiblePaths & otherPossiblePaths);
    if (commonPaths == 0) {
      return -1;
    } else {
      int path;
      do {
        path = rand.nextInt(4);
      } while ((commonPaths & (1<<path)) == 1);
      return path;
    }
  }

  public int getRandomCommonPath(Random rand, int[] otherPossiblePaths) {
    int commonPaths = 0;
    for (int i = 0; i < 4; ++i) {
      if (otherPossiblePaths[i] > 0 && this.hasPath(i)) {
        commonPaths |= (1<<i);
      }
    }
  
    if (commonPaths == 0) {
      return -1;
    } else {
      int path;
      do {
        path = rand.nextInt(4);
      } while ((commonPaths & (1<<path)) == 1);
      return path;
    }
  }
}