/**
 * [Placeable]
 * An interface for anything that needs to be placed.
 * 2020-01-09
 * @version 0.1
 * @author Joseph Wang
 */

public interface Placeable {
  /**
   * [placeItem]
   * Retrieves and returns the associated item that needs to be placed.
   * @return TileComponent, with the item that should be placed at the use location.
   */
  public TileComponent placeItem();
}