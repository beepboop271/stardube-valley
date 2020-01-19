import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * [Shop]
 * 2020-01-14
 * @version 0.2
 * @author Candice Zhang
 */

public class Shop extends IntrinsicTileComponent implements Drawable {
  private LinkedHashMap<String, Double> priceList;
  private final String[] items;
  
  Shop(String name, String imagesPath, double[] offsets, String priceListPath) throws IOException {
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
    this.items = keys.toArray(new String[keys.size()]);
    
  }

  public boolean hasItem(String item) {
    return this.priceList.containsKey(item);
  }

  public double getPriceOf(String item) {
    if (this.hasItem(item)) {
      return this.priceList.get(item);
    }
    return -1;
  }

  public String[] getItems() {
    return this.items;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }

}