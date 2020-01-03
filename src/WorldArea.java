import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

/**
 * [WorldArea] 
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public class WorldArea extends Area {
  // private TownBuilding[] houses;

  HashSet<Tile> forageableTiles = new HashSet<>(); // TODO: replace with int
  ArrayList<Tile> treeTiles = new ArrayList<>();
  Random random = new Random();
  ArrayList<Tile> grassTiles = new ArrayList<>();
  String[] forageables = {"Turnip", "Daffodil", "Leek", "Bluebell", "IceCream", "Javacake", "Mushroom",
                          "Winterroot"};
  String[] trees = {"OakTree", "SpruceTree"};

  public WorldArea(String name,
                   int width, int height) {
    super(name, width, height);
  }

  private void spawnForageables() {
    int maxToSpawn = 6 - forageableTiles.size();
    int spawnNum = Math.min(random.nextInt(3) + 2, maxToSpawn);
    for (int i = 0; i < spawnNum; i++) {
      TileComponent forageable;
      // just realized that the below doesn't work if we're over a year :D
      if ((double)this.getCurrentDay()/28.0 < 1) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[random.nextInt(3)]);
      } else if ((double)this.getCurrentDay()/28.0 < 2) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[random.nextInt(2)]+3);
      } else if ((double)this.getCurrentDay()/28.0 < 3) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[random.nextInt(2)]+5);
      } else {
        forageable = IntrinsicTileComponentFactory.getComponent(forageables[7]);
      }
      
      Tile spawnTile = grassTiles.get(random.nextInt(grassTiles.size()));
      if (spawnTile.getContent() == null) {
        spawnTile.setContent(forageable);
        forageableTiles.add(spawnTile);
      }
    }
  }

  private void spawnTrees() {
    int spawnNum = random.nextInt(10) + 1;
    for (int i = 0; i < spawnNum; i++) {
      TileComponent tree = IntrinsicTileComponentFactory.getComponent(trees[random.nextInt(trees.length)]);
      int y = random.nextInt(this.getMap().length);
      Tile spawnTile = this.getMapAt(random.nextInt(this.getMap()[y].length), y);
      if (spawnTile != null && spawnTile.getContent() == null) {
        spawnTile.setContent(tree);
        treeTiles.add(spawnTile);
      }
    }
  }

  @Override
  public void doDayEndActions() {
    if (this.getCurrentDay()%10 == 0) {
      spawnTrees();
    }
    spawnForageables();
  }

  @Override
  public void setMapAt(Tile t) {
    super.setMapAt(t);
    if (t instanceof GrassTile) {
      this.grassTiles.add(t);
    }
  }

}