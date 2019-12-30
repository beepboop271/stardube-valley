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
  private static ArrayList<CollectableComponent> forageables = new ArrayList<>(); 
  
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

    int numComponents;
    IntrinsicTileComponent componentToAdd;
    String[] nextLine;
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/HarvestableComponents"));
      numComponents = Integer.parseInt(input.readLine());
      for (int i = 0; i < numComponents; ++i) {
        nextLine = input.readLine().split("\\s+");
        // System.out.println("Loading" + nextLine[0]);
        componentToAdd = new IntrinsicHarvestableComponent(nextLine[0],
                                                           "assets/images"+nextLine[1],
                                                           nextLine[2],
                                                           Integer.parseInt(nextLine[3]),
                                                           Integer.parseInt(nextLine[4]));
        // TODO: uncomment after holdables added
        // for (int j = 0; j < Integer.parseInt(nextLine[4]); ++j) {
        //   componentToAdd.setProduct(j, new HoldableDrop(nextLine[5+(j*3)],
        //                                                 Integer.parseInt(nextLine[6+(j*3)]),
        //                                                 Integer.parseInt(nextLine[7+(j*3)])));
        // }
        componentPool.put(componentToAdd.getName(), componentToAdd);
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/CollectableComponents"));
      numComponents = Integer.parseInt(input.readLine());
      for (int i = 0; i < numComponents; ++i) {
        nextLine = input.readLine().split("\\s+");
        // System.out.println("Loading" + nextLine[0]);
        componentToAdd = new CollectableComponent(nextLine[0],
                                                  "assets/images"+nextLine[1],
                                                  1);
        // TODO: uncomment when drops added
        componentToAdd.setProduct(0, new HoldableDrop(1, 1, nextLine[2]));
        if (nextLine[3].equals("y")) {
          forageables.add((CollectableComponent)componentToAdd);
        }
        componentPool.put(componentToAdd.getName(), componentToAdd);
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/Crops"));
      numComponents = Integer.parseInt(input.readLine());
      for (int i = 0; i < numComponents; ++i) {
        nextLine = input.readLine().split("\\s+");
        // System.out.println("Loading" + nextLine[0]);
        componentToAdd = new IntrinsicCrop(nextLine[0], 
                                          "assets/images" + nextLine[1], 
                                          nextLine[3], 
                                          Arrays.copyOfRange(nextLine, 5, nextLine.length));
        componentToAdd.setProduct(0, 
                                  new HoldableDrop(1, Integer.parseInt(nextLine[4]), nextLine[2]));
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

  public static CollectableComponent getRandomForageable(Random random) {
    return IntrinsicTileComponentFactory.forageables.get(
              random.nextInt(IntrinsicTileComponentFactory.forageables.size()));
  }
}