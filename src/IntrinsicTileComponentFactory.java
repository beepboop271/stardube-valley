import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

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

    IntrinsicTileComponent componentToAdd;
    
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/HarvestableComponents"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        int[] offsets = {Integer.parseInt(nextLineData[2]), Integer.parseInt(nextLineData[3])};
        componentToAdd = new IntrinsicHarvestableComponent(nextLineData[0],
                                                           "assets/images"+nextLineData[1]+nextLineData[0]+".png",
                                                           nextLineData[4],
                                                           Integer.parseInt(nextLineData[5]),
                                                           Integer.parseInt(nextLineData[6]),
                                                           offsets);

        componentPool.put(componentToAdd.getName(), componentToAdd);
        for (int j = 0; j < Integer.parseInt(nextLineData[6]); ++j) {
          ((CollectableComponent)componentToAdd).setProduct(j, 
                                          new HoldableDrop(Integer.parseInt(nextLineData[8+(j*3)]),
                                          Integer.parseInt(nextLineData[9+(j*3)]),
                                          nextLineData[7+(j*3)]));
        }
        lineToRead = input.readLine();  
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/CollectableComponents"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        int[] offsets = {Integer.parseInt(nextLineData[4]), Integer.parseInt(nextLineData[5])};
        componentToAdd = new CollectableComponent(nextLineData[0],
                                                  "assets/images"+nextLineData[1]+nextLineData[0]+".png",
                                                  1, offsets);
        ((CollectableComponent)componentToAdd).setProduct(0, new HoldableDrop(1, 1, nextLineData[2]));
        componentPool.put(componentToAdd.getName(), componentToAdd);

        lineToRead = input.readLine();
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/Crops"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        int[] offsets = {Integer.parseInt(nextLineData[5]), Integer.parseInt(nextLineData[6])};
        componentToAdd = new IntrinsicCrop(nextLineData[0], 
                                          "assets/images" + nextLineData[1], 
                                          nextLineData[3], 
                                          Arrays.copyOfRange(nextLineData, 8, nextLineData.length),
                                          offsets, Integer.parseInt(nextLineData[7]));
        ((CollectableComponent)componentToAdd).setProduct(0, 
                                  new HoldableDrop(1, Integer.parseInt(nextLineData[4]), nextLineData[2]));
        
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/Trees"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        int[] offsets = {Integer.parseInt(nextLineData[11]), Integer.parseInt(nextLineData[12])};
        componentToAdd = new IntrinsicTree(nextLineData[0], 
                                          "assets/images"+nextLineData[1],
                                          nextLineData[2], Integer.parseInt(nextLineData[3]),
                                          Integer.parseInt(nextLineData[4]),
                                          Arrays.copyOfRange(nextLineData, 13, nextLineData.length),
                                          offsets);
        for (int j = 0; j < Integer.parseInt(nextLineData[4]); ++j) {
          ((CollectableComponent)componentToAdd).setProduct(j, 
                                    new HoldableDrop(Integer.parseInt(nextLineData[6+(j*3)]),
                                    Integer.parseInt(nextLineData[7+(j*3)]),
                                    nextLineData[5+(j*3)]));
        }
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();  
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/Machines"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        int[] offsets = {Integer.parseInt(nextLineData[2]), Integer.parseInt(nextLineData[3])};
        componentToAdd = new IntrinsicMachine(nextLineData[0], 
                                            "assets/images"+nextLineData[1],
                                            offsets, 
                                            Arrays.copyOfRange(nextLineData, 6, nextLineData.length - 1),
                                            Integer.parseInt(nextLineData[5]),
                                            Integer.parseInt(nextLineData[4]),
                                            nextLineData[nextLineData.length - 1]);
        ((CollectableComponent)componentToAdd).setProduct(0, 
                          new HoldableDrop(1, 1, nextLineData[0] + "Item"));                                    
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();  
      }
      input.close();
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