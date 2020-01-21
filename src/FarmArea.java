import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * [FarmArea]
 * An area dedicated to growing and harvesting crops.
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang, Paula Yuan
 */

public class FarmArea extends Area {
  private HashSet<GroundTile> editedTiles;
  private String[] trees = {"OakTree", "SpruceTree"};
  private ArrayList<Tile> treeTiles = new ArrayList<>();
  private Random random = new Random();

  /**
   * [FarmArea]
   * Constructor for a new FarmArea.
   * @param name   The name of this farm area.
   * @param width  The tile width of this farm area.
   * @param height The tile height of this farm area.
   */
  public FarmArea(String name,
                  int width, int height) {
    super(name, width, height);
    this.editedTiles = new HashSet<GroundTile>();
  }

  /**
   * [addEditedTile]
   * Adds a tile to this area's collection of edited tiles.
   * @author Joseph Wang
   * @param tile The tile to be added.
   */
  public void addEditedTile(GroundTile tile) {
    this.editedTiles.add(tile);
  }

  /**
   * [removeEditedTile]
   * Removes a tile from this area's collection of edited tiles.
   * @author Joseph Wang
   * @param tile The tile to be removed.
   */
  public void removeEditedTile(GroundTile tile) {
    this.editedTiles.remove(tile);
  }

  /**
   * [hasTile]
   * Checks if a tile exists in thsi area's collection of edited
   * tiles.
   * @param tile The tile to check for.
   * @return boolean, true if the tile exists, false otherwise.
   */
  public boolean hasTile(GroundTile tile) {
    return this.editedTiles.contains(tile);
  }

  /**
   * [spawnTrees]
   * Spawns a certain quantity of trees at random locations throughout
   * the farm area.
   * @author Paula Yuan
   */
  private void spawnTrees() {
    int spawnNum = this.random.nextInt(10) + 20;
    if (this.treeTiles.size() >= 30) { // max 30 trees
      spawnNum = 0;
    }
    for (int i = 0; i < spawnNum; i++) {
      String tree = trees[this.random.nextInt(trees.length)];
      int y = this.random.nextInt(this.getMap().length);
      Tile spawnTile = this.getMapAt(this.random.nextInt(this.getMap()[y].length), y);
      Tile centerTile;
      if ((spawnTile != null) && this.inMap(spawnTile.getX()-2, spawnTile.getY()-1)) {
        centerTile = this.getMapAt(spawnTile.getX()-2, spawnTile.getY()-1);
      } else {
        centerTile = null;
      }
      if ((spawnTile != null) && (spawnTile instanceof GroundTile)
            && (centerTile != null) && (centerTile instanceof GroundTile)
            && (spawnTile.getContent() == null)) {
        ExtrinsicTree newTree = new ExtrinsicTree(tree);
        newTree.setStage(17);
        spawnTile.setContent(newTree);
        this.treeTiles.add(spawnTile);
      }
    }
  }

  /**
   * [doDayEndActions]
   * Performs this area's day end actions, like growing crops,
   * spawning trees, or removing tiles from this edited tiles list.
   * @author Joseph Wang, Paula Yuan
   */
  @Override
  public void doDayEndActions() {
    if (this.getCurrentDay() == 1) {
      this.spawnTrees();
    }
    Iterator<GroundTile> allEditedTiles = this.editedTiles.iterator();
    Iterator<Tile> allTreeTiles = this.treeTiles.iterator();

    while (allTreeTiles.hasNext()) {
      Tile currentTile = allTreeTiles.next();
      TileComponent currentContent = currentTile.getContent();
      if ((currentContent != null) && (currentContent instanceof ExtrinsicTree)) {
        ((ExtrinsicTree)currentTile.getContent()).grow();
      }
    }

    while (allEditedTiles.hasNext()) {
      GroundTile currentTile = allEditedTiles.next();
      currentTile.determineImage(this.getCurrentDay());

      if (currentTile.getContent() != null) {
        TileComponent content = currentTile.getContent();
        if (content instanceof ExtrinsicCrop) {
          if (this.getSeason() != ((IntrinsicCrop)((ExtrinsicCrop)content).getIntrinsicSelf()).getPlantingSeason()) {
            currentTile.setContent(null); 
          } else {
            if (currentTile.getLastWatered() == this.getCurrentDay() - 1) {
              ((ExtrinsicCrop)currentTile.getContent()).grow();
            //- Kill the crop if it wasn't watered for some time
            } else if (currentTile.getLastWatered() == this.getCurrentDay() - 3) {
              currentTile.setContent(null);
            }
          }
        }
      } else {
        //- If the tile is tilled but there's nothing on it, untill it
        if (currentTile.getTilledStatus()) { 
          currentTile.setTilledStatus(false);
          currentTile.determineImage(this.getCurrentDay());
          //- Remove the tile from the iterator to prevent ConcurrentModificationException
          allEditedTiles.remove();
          this.editedTiles.remove(currentTile);
        }
      }
    }
  }
}