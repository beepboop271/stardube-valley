import java.io.IOException;

/**
 * [PlaceableItem]
 * 2020-01-09
 * @version 0.1
 * @author Joseph Wang
 */

public class PlaceableItem extends Item implements Placeable {
  private String itemToPlace;

  public PlaceableItem(String name, String description, 
                      String imagePath, String itemToPlace) throws IOException {
    super(name, description, imagePath);

    this.itemToPlace = itemToPlace;
  }

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