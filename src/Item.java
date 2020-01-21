import java.io.IOException;

/**
 * [Item]
 * A class for any holdable item.
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

public class Item extends Holdable {
  
  /**
   * [Item]
   * Constructor for a new Item.
   * @param name        String, the name of the item.
   * @param description String, a description of the item.
   * @param imagePath   String, the path to the image of the item.
   * @throws IOException
   */
  public Item(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }
}