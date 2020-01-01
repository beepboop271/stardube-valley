import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * [HoldableFactory]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
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
    String[] nextLine;
    UtilityTool tool;
    HoldableConsumable drop;
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/UtilityTools"));
      int n = Integer.parseInt(input.readLine());
      for (int i = 0; i < n; ++i) {
        nextLine = input.readLine().split("\\s+");
        tool = new UtilityTool(nextLine[0], nextLine[2], "assets/images/"+nextLine[1]+".png");
        HoldableFactory.holdablePool.put(tool.getName(), tool);
      }
      
      input.close();

      input = new BufferedReader(new FileReader("assets/gamedata/ForageableDrops"));
      n = Integer.parseInt(input.readLine());
      for (int i = 0; i < n; ++i) {
        nextLine = input.readLine().split("\\s+");
        // TODO: fix description
        drop = new HoldableConsumable(nextLine[0]+"Item", "eh?", 
                                      "assets/images/"+nextLine[1]+".png"); 
      }

      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // load other tool(s)
    // fishing rod
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/FishingRod"));
      nextLine = input.readLine().split("\\s+");
        FishingRod rod = new FishingRod(nextLine[0], nextLine[2], "assets/images/"+nextLine[1]+".png");
        HoldableFactory.holdablePool.put(rod.getName(), rod);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // load consumables
    //try {
    //  input = new BufferedReader(new FileReader("assets/gamedata/Consumables"));
    //  while((lineToRead = input.readLine()) != null){
    //    if (lineToRead.length()==0){
    //      break;
    //    }
    //    nextLineData = lineToRead.split("\\s+");
    //    Consumable consumable = new Consumable(stuff);
    //    HoldableFactory.holdablePool.put(consumable.getName(), consumable);
    //  }
    //} catch (IOException e) {
    //  e.printStackTrace();
    //}
  }

  public static Holdable getHoldable(String holdable) {
    if (!HoldableFactory.isInitialized) {
      throw new RuntimeException("HoldableFactory not initialized");
    }
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}