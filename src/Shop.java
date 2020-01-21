import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * [Shop]
 * An IntrinsicTileComponent that stores essential data for shop interations,
 * including items it sells and a price list.
 * 2020-01-20
 * @version 0.4
 * @author Candice Zhang
 */

public class Shop extends IntrinsicTileComponent implements Drawable {
  private LinkedHashMap<String, Double> priceList;
  private final String[] ITEMS;
  
  /**
   * [Shop]
   * Constructor for a new Shop.
   * @param name          Name of the shop.
   * @param description   Description of the shop.
   * @param imagePath     Image path of the shop.
   * @param offsets       The offsets (in tiles) that should be considered during drawing.
   * @param priceListPath The path for the price list of this shop.
   * @throws IOException
   */
  public Shop(String name, String imagesPath, double[] offsets, String priceListPath) throws IOException {
    super(name, imagesPath, offsets);
    this.priceList = new LinkedHashMap<String, Double>();
    try {
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/"+priceListPath));
      String lineToRead = input.readLine();
      String[] nextLineData;
      while(lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        priceList.put(nextLineData[0], Double.parseDouble(nextLineData[1]));
        lineToRead = input.readLine();
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    ArrayList<String> keys = new ArrayList<String>(this.priceList.keySet());
    this.ITEMS = keys.toArray(new String[keys.size()]);
  }

  /**
   * [hasItem]
   * Checks if the shop sells the given item.
   * @param item String, name of the item.
   * @return boolean, true if the shop has the given item, false otherwise.
   */
  public boolean hasItem(String item) {
    return this.priceList.containsKey(item);
  }

  /**
   * [getPriceOf]
   * Retrieves the price of the given item.
   * @param item String, name of the item.
   * @return double, price of the given item (-1 if the shop does not sell this item).
   */
  public double getPriceOf(String item) {
    if (this.hasItem(item)) {
      return this.priceList.get(item);
    }
    return -1;
  }

  /**
   * [getItems]
   * Retrieves all items this shop sells.
   * @return String[], a String array containing all items this shop sells.
   */
  public String[] getItems() {
    return this.ITEMS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}