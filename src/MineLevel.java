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
  private static final int LEVELS_PER_TIER = 20;

  private final int level;
  private Gateway entranceGateway;

  public static class Builder {
    private String name;
    private int level;
    
    private int width;
    private int height;
    private MineLevelComponent[] components;
    private Point[] componentPoints;

    public Builder(int level) {
      if (!MineLevel.componentsInitialized) {
        throw new RuntimeException("MineLevelComponents not initialized");
      }
      this.name = String.format("MineLevel%d", level);
      this.level = level;

      this.width = 0;
      this.height = 0;

      int size = (int)((Math.random()*4)+1+level/LEVELS_PER_TIER);
      this.componentPoints = new Point[size];
      this.components = new MineLevelComponent[size];
    }

    public MineLevel buildLevel(MineArea containingArea) {
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
      
      this.assembleArea(level);
      this.setExitGateway(level, containingArea);

      return level;
    }

    private void chooseRandomComponents() {
      int numComponents = 1;
      int nextDirection;
      Point anchorToPrevious, anchorToNext;
      MineLevelComponent existingComponent, newComponent;

      while (numComponents < this.components.length) {
        int existingIdx = (int)(Math.random()*numComponents);
        existingComponent = this.components[existingIdx];
        newComponent = MineLevel.levelComponents[(int)(Math.random()*MineLevel.numLevelComponents)];
        nextDirection = existingComponent.getRandomCommonPath(newComponent.getPossiblePaths());

        if (nextDirection > -1) {
          this.components[numComponents] = newComponent;
          anchorToPrevious = existingComponent.getRandomPathInDirection(nextDirection);
          anchorToNext = newComponent.getRandomPathInDirection(World.getOppositeDirection(nextDirection));
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

    private void assembleArea(MineLevel level) {
      Point offset;
      int realX, realY;
      int i = -1;
      int numLaddersCreated = 0;
      while (++i < this.components.length && this.components[i] != null) {
        offset = this.componentPoints[i];
        for (int y = 0; y < this.components[i].getHeight(); ++y) {
          for (int x = 0; x < this.components[i].getWidth(); ++x) {
            if (this.components[i].isWalkableAt(x, y)) {
              realX = (int)offset.x+x+1;
              realY = (int)offset.y+y+1;
              level.setMapAt(new GroundTile(realX, realY).setMineImage());
              // ores here
              if (Math.random() < 0.3) {      // 30% to be occupied
                if (Math.random() < 0.15) {   //   15% to be special
                  if (Math.random() < 0.1) {  //     10% to be very special
                    String[] choices = MineLevel.SPECIAL_ORES[Math.min(MineLevel.SPECIAL_ORES.length, 
                                                                       this.level/MineLevel.LEVELS_PER_TIER)];
                    if (choices.length > 0) {
                      level.addHarvestableAt(realX, realY, choices[(int)(Math.random()*choices.length)]);
                    } else {
                      level.addHarvestableAt(realX, realY, MineLevel.METAL_ORES[Math.min(MineLevel.METAL_ORES.length,
                                                                                         this.level/MineLevel.LEVELS_PER_TIER)]);
                    }
                  } else {                    //     90% to be an ore
                    level.addHarvestableAt(realX, realY, MineLevel.METAL_ORES[Math.min(MineLevel.METAL_ORES.length,
                                                                                       this.level/MineLevel.LEVELS_PER_TIER)]);
                  }
                } else {                      //   85% to be rock
                  if (Math.random() < 0.1) {  //     10% to be hard rock
                    level.addHarvestableAt(realX, realY, "HardRock");
                  } else {                    //     90% to be normal rock
                    level.addHarvestableAt(realX, realY, "Rock");
                  }
                }

                if (Math.random() < 0.06 && this.level != MineArea.NUM_LEVELS-1) {
                  ++numLaddersCreated;
                  level.addGateway(new Gateway(realX, realY, Gateway.OMNIDIRECTIONAL, true));
                }
              }
            }
          }
        }
      }
      if ((numLaddersCreated == 0) && (this.level != MineArea.NUM_LEVELS-1)) {
        int randomComponentIdx = (int)(Math.random()*this.components.length);
        Point randomPosition;
        do {
          randomPosition = new Point((int)(Math.random()*this.components[randomComponentIdx].getWidth()),
                                     (int)(Math.random()*this.components[randomComponentIdx].getHeight()));
          randomPosition.translate(this.componentPoints[randomComponentIdx].x+1,
                                   this.componentPoints[randomComponentIdx].y+1);
        } while (level.getMapAt(randomPosition) == null);

        level.addGateway(new Gateway((int)(randomPosition.x), (int)(randomPosition.y),
                                     Gateway.OMNIDIRECTIONAL, true));
        if (level.getMapAt(randomPosition).getContent() == null) {
          level.setMapAt(new MineGatewayTile((int)(randomPosition.x), (int)(randomPosition.y),
                                             MineGatewayTile.DOWNWARDS_LADDER));
        }
      }
    }

    private void setExitGateway(MineLevel level, MineArea containingArea) {
      for (int y = 1; y < this.height; ++y) {
        for (int x = 1; x < this.width; ++x) {
          if (level.getMapAt(x, y) != null) {
            level.setMapAt(new MineGatewayTile(x, y-1, MineGatewayTile.UPWARDS_LADDER));
            level.setMapAt(new GroundTile(x, y).setMineImage());  // so the player doesn't spawn on a rock
            Gateway exit = new Gateway(x, y-1, World.NORTH, true);
            exit.setDestinationArea(containingArea);
            exit.setDestinationGateway(containingArea.getLevelExitGateway());
            level.addGateway(exit);
            level.entranceGateway = exit;
            return;
          }
        }
      }
    }
  }

  private MineLevel(Builder b) {
    super(b.name, b.width, b.height);
    this.level = b.level;
  }

  public int getLevel() {
    return this.level;
  }

  @Override
  public void removeComponentAt(Point pos) {
    super.removeComponentAt(pos);
    if (this.getGateway(pos) != null) {
      this.setMapAt(new MineGatewayTile((int)(pos.x), (int)(pos.y), MineGatewayTile.DOWNWARDS_LADDER));
    }
  }

  public Gateway getEntranceGateway() {
    return this.entranceGateway;
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
}