import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * [IntrinsicTileComponentFactory]
 * 2019-12-20
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan, Joseph Wang
 */

public class IntrinsicTileComponentFactory {
  private static boolean isInitialized = false;
  private static HashMap<String, IntrinsicTileComponent> componentPool;
  
  private IntrinsicTileComponentFactory() {
    // do not allow anyone to create an object of this class
  }

  public static void initializeComponents() {
    if (IntrinsicTileComponentFactory.isInitialized) {
      return;
    }
    IntrinsicTileComponentFactory.isInitialized = true;

    IntrinsicTileComponentFactory.componentPool = new HashMap<String, IntrinsicTileComponent>();
    BufferedReader input;
    String lineToRead;
    String[] nextLineData;

    int numComponents;
    IntrinsicTileComponent componentToAdd;
    
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/HarvestableComponents"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        componentToAdd = new IntrinsicHarvestableComponent(nextLineData[0],
                                                           "assets/images"+nextLineData[1],
                                                           nextLineData[2],
                                                           Integer.parseInt(nextLineData[3]),
                                                           Integer.parseInt(nextLineData[4]));

        componentPool.put(componentToAdd.getName(), componentToAdd);
        // TODO: uncomment after holdables added (this may not be relevant anymore)
          // for (int j = 0; j < Integer.parseInt(nextLine[4]); ++j) {
          //   componentToAdd.setProduct(j, new HoldableDrop(nextLine[5+(j*3)],
          //                                                 Integer.parseInt(nextLine[6+(j*3)]),
          //                                                 Integer.parseInt(nextLine[7+(j*3)])));
          // }  
        lineToRead = input.readLine();  
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/CollectableComponents"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        componentToAdd = new CollectableComponent(nextLineData[0],
                                                  "assets/images"+nextLineData[1],
                                                  1);
        // TODO: uncomment when drops added
        componentToAdd.setProduct(0, new HoldableDrop(1, 1, nextLineData[2]));
        componentPool.put(componentToAdd.getName(), componentToAdd);

        lineToRead = input.readLine();
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/Crops"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        componentToAdd = new IntrinsicCrop(nextLineData[0], 
                                          "assets/images" + nextLineData[1], 
                                          nextLineData[3], 
                                          Arrays.copyOfRange(nextLineData, 5, nextLineData.length));
        componentToAdd.setProduct(0, 
                                  new HoldableDrop(1, Integer.parseInt(nextLineData[4]), nextLineData[2]));
        
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static IntrinsicTileComponent getComponent(String component) {
    if (!IntrinsicTileComponentFactory.isInitialized) {
      throw new RuntimeException("IntrinsicTileComponentFactory not initialized");
    }
    return IntrinsicTileComponentFactory.componentPool.get(component);
  }
}