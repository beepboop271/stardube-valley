import java.util.HashSet;
import java.util.Iterator;

/**
 * [FarmArea]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */

public class FarmArea extends Area {
  //private FarmBuilding[] buildings;
  private HashSet<GroundTile> editedTiles;

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

  @Override
  public void doDayEndActions() {
    Iterator<GroundTile> allEditedTiles = this.editedTiles.iterator();

    while (allEditedTiles.hasNext()) {
      GroundTile currentTile = allEditedTiles.next();
      currentTile.determineImage(this.getCurrentDay() + 1);

      if (currentTile.getContent() != null) {
        if (currentTile.getLastWatered() == this.getCurrentDay()) {
          ((ExtrinsicCrop)currentTile.getContent()).grow();
        }
      } else {
        //- If the tile is tilled but there's nothing on it, untill it
        if (currentTile.getTilledStatus() == true) { 
          currentTile.setTilledStatus(false);
          currentTile.determineImage(this.getCurrentDay() + 1);
          //- Remove the tile from the iterator to prevent ConcurrentModificationException
          allEditedTiles.remove();
          this.editedTiles.remove(currentTile);
        }
      }
    }


  }
}