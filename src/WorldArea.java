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

  private Random random = new Random();
  private int numForageableTiles = 0;
  private ArrayList<Tile> treeTiles = new ArrayList<>();
  private ArrayList<Tile> grassTiles = new ArrayList<>();
  private String[] forageables = {"Turnip", "Daffodil", "Leek", "Bluebell", "IceCream", 
                                  "Javacake", "Mushroom", "Winterroot"};
  private String[] trees = {"OakTree", "SpruceTree"};

  public WorldArea(String name,
                   int width, int height) {
    super(name, width, height);
  }

  private void spawnForageables() {
    int maxToSpawn = 6 - numForageableTiles;
    int spawnNum = Math.min(this.random.nextInt(3) + 2, maxToSpawn);
    System.out.println("day: " + (double)this.getCurrentDay() + " spawnNum: " + spawnNum);
    System.out.println(numForageableTiles);
    for (int i = 0; i < spawnNum; i++) {
      TileComponent forageable;
      if (((double)this.getCurrentDay()%112)/28.0 < 1) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[this.random.nextInt(3)]);
      } else if (((double)this.getCurrentDay()%112)/28.0 < 2.0) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[this.random.nextInt(2)+3]);
      } else if (((double)this.getCurrentDay()%112)/28.0 < 3) {
        forageable = IntrinsicTileComponentFactory.getComponent(
                                forageables[this.random.nextInt(2)+5]);
      } else {
        forageable = IntrinsicTileComponentFactory.getComponent(forageables[7]);
      }
      
      Tile spawnTile = this.grassTiles.get(this.random.nextInt(grassTiles.size()));
      if (spawnTile.getContent() == null) {
        spawnTile.setContent(forageable);
        numForageableTiles++;
      }
    }
  }

  private void spawnTrees() {
    int spawnNum = this.random.nextInt(10) + 10;
    if (this.treeTiles.size() >= 20) { // max 20 trees
      spawnNum = 0;
    }
    for (int i = 0; i < spawnNum; i++) {
      String tree = trees[this.random.nextInt(trees.length)];
      int y = this.random.nextInt(this.getMap().length);
      Tile spawnTile = this.getMapAt(this.random.nextInt(this.getMap()[y].length), y);
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
        ExtrinsicTree newTree = new ExtrinsicTree(tree);
        if (((int)this.getCurrentDay()) == 1) {
          newTree.setStage(17);
        }
        spawnTile.setContent(newTree);
        this.treeTiles.add(spawnTile);
      }
    }
  }

  public int getNumForageableTiles() {
    return this.numForageableTiles;
  }

  @Override
  public void doDayEndActions() {
    
    if (this.getCurrentDay()%10 == 0) {
      spawnTrees();
    }
    spawnForageables();

    if (this.getCurrentDay()%28 == 0) {
      for (int i = 0; i < grassTiles.size(); i++) {
        if (this.grassTiles.get(i).getContent() instanceof CollectableComponent) {
          this.grassTiles.get(i).setContent(null);
          this.numForageableTiles--;
        }
      }
    }

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