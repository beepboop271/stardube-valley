import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * [Shop]
 * 2020-01-14
 * @version 0.1
 * @author Candice Zhang
 */

public class Shop {
  private LinkedHashMap<String, Double> priceList;
  private final String[] items;
  
  Shop(String fileName) {
    this.priceList = new LinkedHashMap<String, Double>();
    try {
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/"+fileName));
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
}