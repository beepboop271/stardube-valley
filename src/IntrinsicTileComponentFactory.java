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
  
  /**
   * [IntrinsicTileComponentFactory]
   * Constructor for a new IntrinsicTileComponentFactory. It is private
   * so that an object of this class cannot be made.
   */
  private IntrinsicTileComponentFactory() {
    // do not allow anyone to create an object of this class
  }

  /**
   * [initializeComponents]
   * Reads gamedata files and initialzes the intrinsic tile components.
   */
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
        double[] offsets = {Double.parseDouble(nextLineData[2]), Double.parseDouble(nextLineData[3])};
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
        double[] offsets = {Double.parseDouble(nextLineData[4]), Double.parseDouble(nextLineData[5])};
        componentToAdd = new CollectableComponent(nextLineData[0],
                                                  "assets/images"+nextLineData[1]+nextLineData[0]+".png",
                                                  1, offsets);
        ((CollectableComponent)componentToAdd).setProduct(0, new HoldableDrop(1, 1, nextLineData[2]));
        componentPool.put(componentToAdd.getName(), componentToAdd);

        lineToRead = input.readLine();
      }
      input.close();

      // load bushes
      input = new BufferedReader(new FileReader("assets/gamedata/Bushes"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[5]), Double.parseDouble(nextLineData[6])};
        componentToAdd = new IntrinsicGrowableCollectable(nextLineData[0], 
                                                          "assets/images" + nextLineData[1],
                                                          nextLineData[3],
                                                          Arrays.copyOfRange(nextLineData, 7, 
                                                          nextLineData.length),
                                                          offsets);
        ((CollectableComponent)componentToAdd).setProduct(0,
                                  new HoldableDrop(1, Integer.parseInt(nextLineData[4]), nextLineData[2]));
        componentPool.put(componentToAdd.getName(), componentToAdd);

        lineToRead = input.readLine();
      }
      input.close();

      // load crops
      input = new BufferedReader(new FileReader("assets/gamedata/Crops"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[6]), Double.parseDouble(nextLineData[7])};
        componentToAdd = new IntrinsicCrop(nextLineData[0], 
                                          "assets/images" + nextLineData[1], 
                                          nextLineData[3], 
                                          Arrays.copyOfRange(nextLineData, 9, nextLineData.length),
                                          offsets, Integer.parseInt(nextLineData[8]));
        ((CollectableComponent)componentToAdd).setProduct(0, 
                                  new HoldableDrop(Integer.parseInt(nextLineData[4]), 
                                                    Integer.parseInt(nextLineData[5]), 
                                                    nextLineData[2]));
        
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();
      }
      input.close();

      // load trees
      input = new BufferedReader(new FileReader("assets/gamedata/Trees"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[11]), Double.parseDouble(nextLineData[12])};
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

      // load machines
      input = new BufferedReader(new FileReader("assets/gamedata/Machines"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[2]), Double.parseDouble(nextLineData[3])};
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

      // load furniture
      input = new BufferedReader(new FileReader("assets/gamedata/Furniture"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[2]), Double.parseDouble(nextLineData[3])};
        componentToAdd = new Furniture(nextLineData[0], 
                                      "assets/images"+nextLineData[1] +".png",
                                      offsets);
        ((CollectableComponent)componentToAdd).setProduct(0, 
                          new HoldableDrop(1, 1, nextLineData[0] + "Item"));                                    
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();  
      }
      input.close();

      // load buildings
      input = new BufferedReader(new FileReader("assets/gamedata/Buildings"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[2]), Double.parseDouble(nextLineData[3])};
        componentToAdd = new Building(nextLineData[0], 
                                            "assets/images"+nextLineData[1]+".png",
                                            offsets);                              
        componentPool.put(componentToAdd.getName(), componentToAdd);
        lineToRead = input.readLine();  
      }
      input.close();

      // load shops
      input = new BufferedReader(new FileReader("assets/gamedata/Shops"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[1]), Double.parseDouble(nextLineData[2])};
        Shop shop = new Shop(nextLineData[0], "assets/images"+nextLineData[3]+".png", offsets, nextLineData[4]);
        componentPool.put(shop.getName(), shop);
        lineToRead = input.readLine();
      }
      input.close();

      // load crafting stores
      input = new BufferedReader(new FileReader("assets/gamedata/CraftingStores"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        double[] offsets = {Double.parseDouble(nextLineData[1]), Double.parseDouble(nextLineData[2])};
        CraftingStore craftingStore = new CraftingStore(nextLineData[0],
                                      "assets/images"+nextLineData[3]+".png", offsets, nextLineData[4]);
        componentPool.put(craftingStore.getName(), craftingStore);
        lineToRead = input.readLine();
      }
      input.close();

      // load shipping data
      input = new BufferedReader(new FileReader("assets/gamedata/ShippingData"));
      nextLineData = input.readLine().split("\\s+");
      double[] offsets = {Double.parseDouble(nextLineData[3]), Double.parseDouble(nextLineData[4])};
      componentToAdd = new ShippingContainer("assets/images"+nextLineData[1],
                                            nextLineData[2], offsets);
      componentPool.put(componentToAdd.getName(), componentToAdd);                                      
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