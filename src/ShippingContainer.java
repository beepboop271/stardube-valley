import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.image.BufferedImage;

import java.util.HashMap;

/**
 * [ShippingContainer]
 * 2020-01-16
 * @version 0.1
 * @author Joseph Wang
 */

 public class ShippingContainer extends IntrinsicTileComponent implements Drawable {
  private HashMap<String, Integer> sellPrices;

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

  public int sellItem(HoldableStack item) {
    if (sellPrices.get(item.getContainedHoldable().getName()) == null) {
      return 0;
    }
    return sellPrices.get(item.getContainedHoldable().getName()) * item.getQuantity();
  }

  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}