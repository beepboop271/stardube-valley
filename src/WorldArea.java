import java.util.Iterator;
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

  int numForageableTiles = 0;
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
    int maxToSpawn = 6 - numForageableTiles;
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
        numForageableTiles++;
      }
    }
  }

  private void spawnTrees() {
    int spawnNum = random.nextInt(10) + 30;
    for (int i = 0; i < spawnNum; i++) {
      String tree = trees[random.nextInt(trees.length)];
      int y = random.nextInt(this.getMap().length);
      Tile spawnTile = this.getMapAt(random.nextInt(this.getMap()[y].length), y);
      Tile centerTile;
      if (spawnTile != null && this.inMap(spawnTile.getX()-2, spawnTile.getY()-1)) {
        centerTile = this.getMapAt(spawnTile.getX()-2, spawnTile.getY()-1);
      } else {
        centerTile = null;
      }
      if (spawnTile != null && (spawnTile instanceof GroundTile || spawnTile instanceof GrassTile) 
          && centerTile != null
          && (centerTile instanceof GroundTile || centerTile instanceof GrassTile)  
          && spawnTile.getContent() == null) {
        spawnTile.setContent(new ExtrinsicTree(tree));
        treeTiles.add(spawnTile);
      }
    }
  }

  @Override
  public void doDayEndActions() {
    
    if (this.getCurrentDay()%10 == 0 || this.getCurrentDay() == 1) {
      spawnTrees();
    }
    spawnForageables();

    Iterator<Tile> allTreeTiles = this.treeTiles.iterator();

    while (allTreeTiles.hasNext()) {
      Tile currentTile = allTreeTiles.next();
      TileComponent currentContent = currentTile.getContent();
      if (currentContent != null && currentContent instanceof ExtrinsicTree) {
        ((ExtrinsicTree)currentTile.getContent()).grow();
      }
    }
  }

  @Override
  public void setMapAt(Tile t) {
    super.setMapAt(t);
    if (t instanceof GrassTile) {
      this.grassTiles.add(t);
    }
  }

}