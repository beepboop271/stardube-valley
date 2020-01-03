import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * [FarmArea]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang, Paula Yuan
 */

public class FarmArea extends Area {
  //private FarmBuilding[] buildings;
  private HashSet<GroundTile> editedTiles;
  String[] trees = {"OakTree, SpruceTree"};
  ArrayList<Tile> treeTiles= new ArrayList<>();
  Random random = new Random();

  public FarmArea(String name,
                  int width, int height) {
    super(name, width, height);
    this.editedTiles = new HashSet<GroundTile>();
  }

  public void addEditedTile(GroundTile tile) {
    this.editedTiles.add(tile);
  }

  public void removeEditedTile(GroundTile tile) {
    this.editedTiles.remove(tile);
  }

  public boolean hasTile(GroundTile tile) {
    if (this.editedTiles.contains(tile)) {
      return true;
    }

    return false;
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
      this.spawnTrees();
    }
    Iterator<GroundTile> allEditedTiles = this.editedTiles.iterator();

    while (allEditedTiles.hasNext()) {
      GroundTile currentTile = allEditedTiles.next();
      currentTile.determineImage(this.getCurrentDay());

      if (currentTile.getContent() != null) {
        TileComponent content = currentTile.getContent();
        if (content instanceof ExtrinsicCrop) {
          if (this.getSeason() != ((IntrinsicCrop)((ExtrinsicCrop)content).getIntrinsicSelf())
            .getPlantingSeason()) { //for now the crop just disappears
            currentTile.setContent(null); //TODO: make a dead plant appear here lol
          } else {
            if (currentTile.getLastWatered() == this.getCurrentDay() - 1) {
              ((ExtrinsicCrop)currentTile.getContent()).grow();
            }
          }
        }
      } else {
        //- If the tile is tilled but there's nothing on it, untill it
        if (currentTile.getTilledStatus() == true) { 
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