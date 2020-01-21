import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * [HoldableFactory]
 * The factory of all holdables in stardube valley.
 * 2019-12-20
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan, Candice Zhang, Joseph Wang
 */
public class HoldableFactory {
  private static boolean isInitialized = false;
  private static HashMap<String, Holdable> holdablePool;
  
  /**
   * [HoldableFactory]
   * Constructor for a new Holdable.
   * Does not allow anyone to create an object of this class.
   */
  private HoldableFactory(){
  }

  /**
   * [initializeItems]
   * Reads gamedata files and initialzes the holdables.
   */
  public static void initializeItems() {
    if (HoldableFactory.isInitialized) {
      return;
    }
    HoldableFactory.isInitialized = true;

    HoldableFactory.holdablePool = new HashMap<String, Holdable>();
    BufferedReader input;
    String lineToRead;
    String[] nextLineData;

    try {
      // initialize utility tools
      input = new BufferedReader(new FileReader("assets/gamedata/UtilityTools"));
      lineToRead = input.readLine();
      while(lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        UtilityTool tool = new UtilityTool(nextLineData[0], nextLineData[2], 
                                           "assets/images/"+nextLineData[1]+".png",
                                           Integer.parseInt(nextLineData[3]),
                                           Integer.parseInt(nextLineData[4]),
                                           nextLineData[5]);
        HoldableFactory.holdablePool.put(tool.getName(), tool);
        lineToRead = input.readLine();
      }
      input.close();
      // initialize other tools
      // - fishing rod
      input = new BufferedReader(new FileReader("assets/gamedata/FishingRod"));
      nextLineData = input.readLine().split("\\s+");
      FishingRod rod = new FishingRod(nextLineData[0], nextLineData[2], 
                                      "assets/images/"+nextLineData[1]+".png",
                                      Integer.parseInt(nextLineData[3]));
      HoldableFactory.holdablePool.put(rod.getName(), rod);
      input.close();
      // initialize seeds
      input = new BufferedReader(new FileReader("assets/gamedata/Seeds"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        Seeds seed = new Seeds(nextLineData[0],
                               nextLineData[2], 
                               "assets/images/"+nextLineData[1]+".png",
                               nextLineData[3]);

        HoldableFactory.holdablePool.put(seed.getName(), seed);
        lineToRead = input.readLine();
      }
      input.close();
      // initialize consumables
      // - forageable drops
      input = new BufferedReader(new FileReader("assets/gamedata/Consumables"));
      lineToRead = input.readLine();
      while(lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        if (nextLineData.length > 5) {
          SpecialConsumable consumable = new SpecialConsumable(nextLineData[0], 
                                                               nextLineData[2],
                                                               "assets/images/"+nextLineData[1]+nextLineData[0]+".png",
                                                               Integer.parseInt(nextLineData[3]), 
                                                               Integer.parseInt(nextLineData[4]),
                                                               Integer.parseInt(nextLineData[5]),
                                                               Integer.parseInt(nextLineData[6]));
          HoldableFactory.holdablePool.put(consumable.getName(), consumable);
        } else {
          Consumable consumable = new Consumable(nextLineData[0], 
                                                 nextLineData[2], 
                                                 "assets/images/"+nextLineData[1]+nextLineData[0]+".png",
                                                 Integer.parseInt(nextLineData[3]), 
                                                 Integer.parseInt(nextLineData[4]));
          HoldableFactory.holdablePool.put(consumable.getName(), consumable);
        }
        
        lineToRead = input.readLine();
      }
      input.close();
      // initialize items
      input = new BufferedReader(new FileReader("assets/gamedata/Items"));
      lineToRead = input.readLine();
      while (lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        Item item = new Item(nextLineData[0],
                             nextLineData[2],
                             "assets/images"+nextLineData[1]+nextLineData[0]+".png");
        HoldableFactory.holdablePool.put(item.getName(), item);
        lineToRead = input.readLine();
      }
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/PlaceableItems"));
      lineToRead = input.readLine();
      while(lineToRead.length() > 0) {
        nextLineData = lineToRead.split("\\s+");
        PlaceableItem item = new PlaceableItem(nextLineData[0],
                                               nextLineData[2],
                                               "assets/images"+nextLineData[1]+nextLineData[0]+".png",
                                               nextLineData[3]);
        HoldableFactory.holdablePool.put(item.getName(), item);
        lineToRead = input.readLine();
      }
      input.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * [getHoldable]
   * Retrieves the corresponding holdable for the given holdable name.
   * @param holdable The name of the holdable.
   * @return Holdable, the holdable with the given name.
   */
  public static Holdable getHoldable(String holdable) {
    if (!HoldableFactory.isInitialized) {
      throw new RuntimeException("HoldableFactory not initialized");
    }
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}