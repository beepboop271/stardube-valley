import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * [HoldableFactory]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan, Candice Zhang
 */
public class HoldableFactory {
  private static boolean isInitialized = false;
  private static HashMap<String, Holdable> holdablePool;

  private HoldableFactory(){
    // do not allow anyone to create an object of this class
  }

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
      while(lineToRead != null){
        if (lineToRead.length()==0){
          break;
        }
        nextLineData = lineToRead.split("\\s+");
        UtilityTool tool = new UtilityTool(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
        HoldableFactory.holdablePool.put(tool.getName(), tool);
        lineToRead = input.readLine();
      }
      input.close();

      // initialize other tools
      // - fishing rod
      input = new BufferedReader(new FileReader("assets/gamedata/FishingRod"));
      nextLineData = input.readLine().split("\\s+");
      FishingRod rod = new FishingRod(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
      HoldableFactory.holdablePool.put(rod.getName(), rod);
      input.close();

      // initialize consumables
      // - forageable drops
      input = new BufferedReader(new FileReader("assets/gamedata/ForageableDrops"));
      lineToRead = input.readLine();
      while(lineToRead != null){
        if (lineToRead.length()==0){
          break;
        }
        nextLineData = lineToRead.split("\\s+");
        // TODO: fix description
        Consumable drop = new Consumable(nextLineData[0]+"Item", "eh?", 
                                      "assets/images/"+nextLineData[1]+".png"); 
        HoldableFactory.holdablePool.put(drop.getName(), drop);
        lineToRead = input.readLine();
      }
      input.close();
      // - fishable consumables
      input = new BufferedReader(new FileReader("assets/gamedata/FishableConsumables"));
      lineToRead = input.readLine();
      while(lineToRead != null){
        if (lineToRead.length()==0){
          break;
        }
        nextLineData = lineToRead.split("\\s+");
        Consumable consumable = new Consumable(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
        HoldableFactory.holdablePool.put(consumable.getName(), consumable);
        lineToRead = input.readLine();
      }
      input.close();
      
      // initialize items
      input = new BufferedReader(new FileReader("assets/gamedata/Items"));
      lineToRead = input.readLine();
      while(lineToRead != null){
        if (lineToRead.length()==0){
          break;
        }
        nextLineData = lineToRead.split("\\s+");
        Item item = new Item(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
        HoldableFactory.holdablePool.put(item.getName(), item);
        lineToRead = input.readLine();
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static Holdable getHoldable(String holdable) {
    if (!HoldableFactory.isInitialized) {
      throw new RuntimeException("HoldableFactory not initialized");
    }
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}