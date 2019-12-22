import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * [TileComponentFactory]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class TileComponentFactory {
  private static HashMap<String, TileComponent> componentPool;
  
  public static void initializeComponents() {
    TileComponentFactory.componentPool = new HashMap<String, TileComponent>();
    BufferedReader input;

    int numComponents;
    TileComponent componentToAdd;
    String[] nextLine;
    try {
      input = new BufferedReader(new FileReader("/assets/gamedata/HarvestableComponents"));
      numComponents = Integer.parseInt(input.readLine());
      for (int i = 0; i < numComponents; ++i) {
        nextLine = input.readLine().split("\\s+");
        componentToAdd = new HarvestableComponent(nextLine[0],
                                                  "/assets/images/"+nextLine[1]+".png",
                                                  nextLine[2],
                                                  Integer.parseInt(nextLine[3]),
                                                  Integer.parseInt(nextLine[4]));
        for (int j = 0; j < Integer.parseInt(nextLine[4]); ++j) {
          componentToAdd.setProduct(j, new HoldableDrop(nextLine[5+(j*3)],
                                                        Integer.parseInt(nextLine[6+(j*3)]),
                                                        Integer.parseInt(nextLine[7+(j*3)])));
        }
        componentPool.put(componentToAdd.getName(), componentToAdd);
      }
      input.close();

      input = new BufferedReader(new FileReader("/assets/gamedata/CollectableComponents"));
      numComponents = Integer.parseInt(input.readLine());
      for (int i = 0; i < numComponents; ++i) {
        nextLine = input.readLine().split("\\s+");
        componentToAdd = new CollectableComponent(nextLine[0],
                                                  "/assets/images/"+nextLine[1]+".png",
                                                  Integer.parseInt(nextLine[2]));
        for (int j = 0; j < Integer.parseInt(nextLine[2]); ++j) {
          componentToAdd.setProduct(j, new HoldableDrop(nextLine[3+(j*2)],
                                                        Integer.parseInt(nextLine[4+(j*2)]),
                                                        Integer.parseInt(nextLine[4+(j*2)])));
        }
        componentPool.put(componentToAdd.getName(), componentToAdd);
      }
      input.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static TileComponent getComponent(String component) {
    return (TileComponent)TileComponentFactory.componentPool
                                              .get(component);
  }
}