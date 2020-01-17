import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.image.BufferedImage;

import java.util.HashMap;

/**
 * [ShippingContainer]
 * A container that you throw stuff in and it sells what you toss at the end of
 * the day.
 * You cannot retrieve the item you threw in.
 * 2020-01-16
 * @version 0.1
 * @author Joseph Wang
 */

 public class ShippingContainer extends IntrinsicTileComponent implements Drawable {
  private HashMap<String, Integer> sellPrices;

  /**
   * [ShippingContainer]
   * Constructor for a new shipping container. Needs the image path of the container,
   * the path for the data and the image offsets.
   * @author Joseph Wang
   * @param imagesPath The path to the image of the container.
   * @param pricesPath The path to the sell data of the container.
   * @param offsets The total image offsets for this image.
   * @throws IOException
   */
  public ShippingContainer(String imagesPath, String pricesPath, int[] offsets) throws IOException {
    super("ShippingContainer", imagesPath, offsets);

    //- Initialize the selling prices
    this.sellPrices = new HashMap<String, Integer>();
    BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/" + pricesPath));

    input.readLine(); //- Waste the first line that only has the initialization for the factory
    String lineToRead = input.readLine();
    while (lineToRead.length() > 0) {
      String[] priceInfo = lineToRead.split("\\s+");
      sellPrices.put(priceInfo[0], Integer.parseInt(priceInfo[1]));
      lineToRead = input.readLine();
    }

    input.close();
  }

  /**
   * [sellItem]
   * Retrieves the given sell price for a item that is dropped in.
   * @author Joseph Wang
   * @param item The item that needs to be sold.
   * @return int, the amount the item is sold for.
   */
  public int sellItem(HoldableStack item) {
    if (sellPrices.get(item.getContainedHoldable().getName()) == null) {
      return 0;
    }
    return sellPrices.get(item.getContainedHoldable().getName()) * item.getQuantity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}