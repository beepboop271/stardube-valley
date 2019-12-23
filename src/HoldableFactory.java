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
  private static HashMap<String, Holdable> holdablePool;

  private HoldableFactory(){
    // do not allow anyone to create an object of this class
  }

  public static void initializeItems() {
    HoldableFactory.holdablePool = new HashMap<String, Holdable>();
    BufferedReader input;
    String[] nextLine;
    UtilityTool tool;
    try {
      input = new BufferedReader(new FileReader("/assets/gamedata/UtilityTools"));
      for (int i = 0; i < Integer.parseInt(input.readLine()); ++i) {
        nextLine = input.readLine().split("\\s+");
        tool = new UtilityTool(nextLine[0], nextLine[2], "/assets/images/holdables/"+nextLine[1]+".png");
        HoldableFactory.holdablePool.put(tool.getName(), tool);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Holdable getHoldable(String holdable) {
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}