import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * [WorldArea] 
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public class WorldArea extends Area { //TODO: JAVADOCS
  private Random random = new Random();
  private int numForageableTiles = 0;
  private ArrayList<Tile> treeTiles = new ArrayList<>();
  private ArrayList<Tile> bushTiles = new ArrayList<>();
  private ArrayList<Tile> grassTiles = new ArrayList<>();
  private String[] bushes = {"GrapeBush", "BlackberryBush", "HazelnutBush", "OrangeBush", "CoffeeBush"};
  private String[] forageables = {"Turnip", "Daffodil", "Leek", "Bluebell", "IceCream", 
                                  "Javacake", "Mushroom", "Winterroot"};
  private String[] trees = {"OakTree", "SpruceTree"};

  public WorldArea(String name,
                   int width, int height) {
    super(name, width, height);
  }

  private void spawnBushes() {
    int spawnNum = this.random.nextInt(5) + 10;
    if (this.bushTiles.size() >=20) {
      return;
    }
    for (int i = 0; i < spawnNum; i++) {
      TileComponent bush;
      // decides what bushes to spawn based on the season
      if (((double)this.getCurrentDay()%112)/28.0 < 2.0) {
        bush = IntrinsicTileComponentFactory.getComponent(bushes[0]);
      } else if (((double)this.getCurrentDay()%112)/28.0 < 3) {
        bush = IntrinsicTileComponentFactory.getComponent(bushes[this.random.nextInt(2)+1]);
      } else {
        bush = IntrinsicTileComponentFactory.getComponent(bushes[this.random.nextInt(2)+3]);
      }
      ExtrinsicGrowableCollectable newBush = new ExtrinsicGrowableCollectable(
                                                 (IntrinsicGrowableCollectable)bush);
      Tile spawnTile = this.grassTiles.get(this.random.nextInt(grassTiles.size()));
      if (spawnTile.getContent() == null && this.inMap(spawnTile.getX()-1, spawnTile.getY())
          && this.inMap(spawnTile.getX()-1, spawnTile.getY()-1) 
          && this.inMap(spawnTile.getX(), spawnTile.getY()-1)
          && this.getMapAt(spawnTile.getX()-1, spawnTile.getY()) instanceof GrassTile) {
        spawnTile.setContent(newBush);
        bushTiles.add(spawnTile);
      }
    }
  }

  private void spawnForageables() {
    int maxToSpawn = 6 - numForageableTiles;
    int spawnNum = Math.min(this.random.nextInt(3) + 2, maxToSpawn);
    for (int i = 0; i < spawnNum; i++) {
      TileComponent forageable;
      // decides what forageables to spawn based on the season
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
    int spawnNum = this.random.nextInt(10) + 20;
    if (this.treeTiles.size() >= 30) { // max 20 trees
      return;
    }
    for (int i = 0; i < spawnNum; i++) {
      String tree = trees[this.random.nextInt(trees.length)];
      Tile spawnTile = this.grassTiles.get(this.random.nextInt(grassTiles.size()));
      Tile centerTile;
      if (this.inMap(spawnTile.getX()-2, spawnTile.getY()-1)) {
        centerTile = this.getMapAt(spawnTile.getX()-2, spawnTile.getY()-1);
      } else {
        centerTile = null;
      }
      if (spawnTile != null && centerTile != null && spawnTile instanceof GrassTile 
          && centerTile instanceof GrassTile && spawnTile.getContent() == null) {
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

  public void setNumForageableTiles(int num) {
    this.numForageableTiles = num;
  }

  @Override
  public void doDayEndActions() {
    if (this.getCurrentDay()%10 == 1) {
      this.spawnTrees();
    }
    this.spawnForageables();

    if (this.getCurrentDay()%28 < 26 && this.getCurrentDay()%28 > 18) {
      for (int i = 0; i < this.bushTiles.size(); i++) {
        int num = this.random.nextInt(4);
        if (num == 1 && this.bushTiles.get(i).getContent() != null) {
          ((ExtrinsicGrowableCollectable)this.bushTiles.get(i).getContent()).grow();
        }
      }
    }
    
    if (this.getCurrentDay()%28-1 == 0) {
      for (int i = 0; i < grassTiles.size(); i++) {
        TileComponent currentContent = this.grassTiles.get(i).getContent();
        if (currentContent instanceof CollectableComponent) {
          this.grassTiles.get(i).setContent(null);
          this.numForageableTiles--;
        } else if (currentContent instanceof ExtrinsicGrowableCollectable) {
          this.grassTiles.get(i).setContent(null);
          this.bushTiles.remove(this.grassTiles.get(i));
        }
      }
      if (((double)this.getCurrentDay()%112)/28.0 > 1) {
        this.spawnBushes();
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