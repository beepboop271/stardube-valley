import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MineLevel extends Area {
  private static MineLevelComponent[] levelComponents;
  private static int numLevelComponents;
  private static boolean componentsInitialized = false;

  private static final String[] METAL_ORES = {
      "CopperOre", "IronOre", "GoldOre"
  };
  private static final String[][] SPECIAL_ORES = {
      {},
      {"AmethystOre", "EmeraldOre"},
      {"RubyOre", "DiamondOre"}
  };
  private static final int LEVELS_PER_TIER = 40;

  private final int level;

  public static class Builder {
    private String name;
    private int level;
    
    private int width;
    private int height;
    private MineLevelComponent[] components;
    private Point[] componentPoints;

    public Builder(int level, int size) {
      if (!MineLevel.componentsInitialized) {
        throw new RuntimeException("MineLevelComponents not initialized");
      }
      this.name = String.format("MineLevel%d", level);
      this.level = level;

      this.width = 0;
      this.height = 0;
      this.componentPoints = new Point[size];
      this.components = new MineLevelComponent[size];
    }

    public MineLevel buildLevel() {
      this.components[0] = MineLevel.levelComponents[(int)(Math.random()*MineLevel.numLevelComponents)];
      this.componentPoints[0] = new Point(0, 0);
      
      this.chooseRandomComponents();
      this.translateToPositive();

      this.width = 0;
      this.height = 0;
      int i = -1;
      while (++i < this.componentPoints.length && this.componentPoints[i] != null) {
        this.width = Math.max(this.width, (int)componentPoints[i].x+components[i].getWidth());
        this.height = Math.max(this.height, (int)componentPoints[i].y+components[i].getHeight());
      }
      this.width += 2;
      this.height += 2;

      MineLevel level = new MineLevel(this);
      
      Point offset;
      int realX, realY;
      i = -1;
      while (++i < this.components.length && this.components[i] != null) {
        offset = this.componentPoints[i];
        for (int y = 0; y < this.components[i].getHeight(); ++y) {
          for (int x = 0; x < this.components[i].getWidth(); ++x) {
            if (this.components[i].isWalkableAt(x, y)) {
              realX = (int)offset.x+x+1;
              realY = (int)offset.y+y+1;
              level.setMapAt(new GroundTile(realX, realY).setMineImage());
              // ores here
              if (Math.random() < 0.4) {      // 40% to be occupied
                if (Math.random() < 0.3) {    //   30% to be special
                  if (Math.random() < 0.1) {  //     10% to be very special
                    String[] choices = MineLevel.SPECIAL_ORES[this.level/MineLevel.LEVELS_PER_TIER];
                    if (choices.length > 0) {
                      level.addHarvestableAt(realX, realY, choices[(int)(Math.random()*choices.length)]);
                    }
                  } else {                    //     90% to be an ore
                    level.addHarvestableAt(realX, realY, MineLevel.METAL_ORES[this.level/MineLevel.LEVELS_PER_TIER]);
                  }
                } else {                      //   70% to be rock
                  if (Math.random() < 0.1) {  //     10% to be hard rock
                    level.addHarvestableAt(realX, realY, "HardRock");
                  } else {                    //     90% to be normal rock
                    level.addHarvestableAt(realX, realY, "Rock");
                  }
                }
              }
            }
          }
        }
      }

      return level;
    }

    private void chooseRandomComponents() {
      int numComponents = 1;
      int nextDirection;
      Point anchorToPrevious, anchorToNext;
      for (int attempt = 0; attempt < this.components.length-1; ++attempt) {
        int existingIdx = (int)(Math.random()*numComponents);
        int allIdx = (int)(Math.random()*MineLevel.numLevelComponents);
        nextDirection = this.components[existingIdx].getRandomCommonPath(MineLevel.levelComponents[allIdx].getPossiblePaths());
        if (nextDirection > -1) {
          this.components[numComponents] = MineLevel.levelComponents[allIdx];
          anchorToPrevious = this.components[existingIdx].getRandomPathInDirection(nextDirection);
          anchorToNext = this.components[numComponents].getRandomPathInDirection(World.getOppositeDirection(nextDirection));
          this.componentPoints[numComponents] = new Point(
              (int)(anchorToPrevious.x+this.componentPoints[existingIdx].x-anchorToNext.x),
              (int)(anchorToPrevious.y+this.componentPoints[existingIdx].y-anchorToNext.y)
          );
          ++numComponents;
        }
      }
    }

    private void translateToPositive() {
      Point translation = new Point(0, 0);
      int i = -1;
      while (++i < this.componentPoints.length && this.componentPoints[i] != null) {
        if (this.componentPoints[i].x < 0 || this.componentPoints[i].y < 0) {
          if (this.componentPoints[i].x < translation.x) {
            translation.x = this.componentPoints[i].x;
          }
          if (this.componentPoints[i].y < translation.y) {
            translation.y = this.componentPoints[i].y;
          }
        }
      }
      if (translation.x != 0 || translation.y != 0) {
        i = -1;
        while (++i < this.componentPoints.length && this.componentPoints[i] != null) {
          this.componentPoints[i].translate(-translation.x, -translation.y);
        }
      }
    }
  }

  private MineLevel(Builder b) {
    super(b.name, b.width, b.height);
    this.level = b.level;
  }

  public static void loadComponents() {
    try {
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/MineLevelComponents"));
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
        MineLevel.levelComponents[i] = componentToAdd;
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getLevel() {
    return this.level;
  }
}