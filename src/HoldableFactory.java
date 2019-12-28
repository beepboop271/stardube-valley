import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * [HoldableFactory]
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
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

    // load utility tools
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/UtilityTools"));
      int n = Integer.parseInt(input.readLine());
      for (int i = 0; i < n; ++i) {
        nextLineData = input.readLine().split("\\s+");
        UtilityTool tool = new UtilityTool(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
        HoldableFactory.holdablePool.put(tool.getName(), tool);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // load other tool(s)
    // fishing rod
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/FishingRod"));
        nextLineData = input.readLine().split("\\s+");
        FishingRod rod = new FishingRod(nextLineData[0], nextLineData[2], "assets/images/"+nextLineData[1]+".png");
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