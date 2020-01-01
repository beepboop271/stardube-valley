import java.io.IOException;

/**
 * [Item]
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

public class Item extends Holdable {
  public Item(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
  }
}