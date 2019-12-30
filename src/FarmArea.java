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
  private HashSet<Tile> tilesWithCrops;

  public FarmArea(String name,
                  int width, int height) {
    super(name, width, height);
  }

  public void addTileWithCrop(Tile tile) {
    this.tilesWithCrops.add(tile);
  }

  public void removeTileWithCrop(Tile tile) {
    this.tilesWithCrops.remove(tile);
  }

  @Override
  public void doDayEndActions() {
    //Iterator<Tile> allTilesWithCrops = this.tilesWithCrops.iterator();

  }
}