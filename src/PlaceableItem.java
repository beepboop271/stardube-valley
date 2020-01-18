import java.io.IOException;

/**
 * [PlaceableItem]
 * A class for items that are able to place a TileComponent onto a tile.
 * 2020-01-09
 * @version 0.2
 * @author Joseph Wang
 */

public class PlaceableItem extends Item implements Placeable {
  private String itemToPlace;

  /**
   * [PlaceableItem]
   * Constructor for the PlaceableItem class.
   * @param name The name of the item.
   * @param description A description of the item.
   * @param imagePath The path to the image.
   * @param itemToPlace The TileComponent to place on the map.
   * @throws IOException
   */
  public PlaceableItem(String name, String description, 
                      String imagePath, String itemToPlace) throws IOException {
    super(name, description, imagePath);

    this.itemToPlace = itemToPlace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileComponent placeItem() {
    //TODO: this is scuffed
    if (this.itemToPlace.equals("Chest")) {
      return new ExtrinsicChest();
    } else {
      TileComponent item =  IntrinsicTileComponentFactory.getComponent(this.itemToPlace);

      if (item instanceof IntrinsicMachine) {
        return new ExtrinsicMachine(this.itemToPlace);
      }
      return item;
    }
  }
}