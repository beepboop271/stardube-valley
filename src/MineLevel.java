import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class MineLevel extends Area {
  private static MineLevelComponent[] levelComponents;
  private static int numLevelComponents;
  private static boolean componentsInitialized = false;
  private final int level;

  public MineLevel(String name, int width, int height, int level, int size) {
    super(name, width, height);
    if (!MineLevel.componentsInitialized) {
      throw new RuntimeException("MineLevelComponents not initialized");
    }
    this.level = level;
    this.buildLevel(size);
  }

  public void buildLevel(int size) {
    Random rand = new Random();
    Rectangle[] componentRects = new Rectangle[size];
    MineLevelComponent[] components = new MineLevelComponent[4];
    // MineLevelComponent nextComponent = 
    components[0] = 
        MineLevel.levelComponents[rand.nextInt(MineLevel.numLevelComponents)];
    
    // LinkedList<Point> possiblePaths = new LinkedList<Point>(nextComponent.getPaths());
    int[] directions = components[0].getNumPathsByDirection();

    componentRects[0] = new Rectangle(0, 0, components[0].getWidth(), components[0].getHeight());
    int numComponents = 1;

    int nextDirection;
    for (int attempt = 0; attempt < size-1; ++attempt) {
      for (int i = 0; i < numComponents; ++i) {
        nextDirection = components[i].getRandomCommonPath(rand, directions);
        if (nextDirection > -1 && Math.random() < 0.5) {
          components[numComponents] = MineLevel.levelComponents[i];
        }
      }
    }
  }

  public static void loadComponents() throws IOException {
    BufferedReader input = new BufferedReader(new FileReader("/assets/gamedata/MineLevelComponents"));
    MineLevel.numLevelComponents = Integer.parseInt(input.readLine());
    MineLevel.levelComponents = new MineLevelComponent[MineLevel.numLevelComponents];
    MineLevel.componentsInitialized = true;

    int width, height;
    MineLevelComponent componentToAdd;
    String[] nextLine;
    for (int i = 0; i < MineLevel.numLevelComponents; ++i) {
      nextLine = input.readLine().split("\\s+");
      width = Integer.parseInt(nextLine[0]);
      height = Integer.parseInt(nextLine[1]);
      componentToAdd = new MineLevelComponent(width, height);
      for (int y = 0; y < height; ++y) {
        nextLine[0] = input.readLine();
        for (int x = 0; x < width; ++x) {
          if (nextLine[0].charAt(x) != ' ') {
            componentToAdd.setWalkableAt(x, y);
            switch (nextLine[0].charAt(x)) {
              case 'n':
                componentToAdd.addPath(World.NORTH, new Point(x, y));
                break;
              case 'e':
                componentToAdd.addPath(World.EAST, new Point(x, y));
                break;
              case 's':
                componentToAdd.addPath(World.SOUTH, new Point(x, y));
                break;
              case 'w':
                componentToAdd.addPath(World.WEST, new Point(x, y));
                break;
            }
          }
        }
      }
    }
    input.close();
  }
}