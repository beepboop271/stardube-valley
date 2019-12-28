import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import java.util.Arrays;

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
    Seeds seed;
    try {
      input = new BufferedReader(new FileReader("assets/gamedata/UtilityTools"));
      int n = Integer.parseInt(input.readLine());
      for (int i = 0; i < n; ++i) {
        nextLine = input.readLine().split("\\s+");
        tool = new UtilityTool(nextLine[0], nextLine[2], "assets/images/"+nextLine[1]+".png");
        HoldableFactory.holdablePool.put(tool.getName(), tool);
      }

      input.close();
      input = new BufferedReader(new FileReader("assets/gamedata/Seeds"));
      int m = Integer.parseInt(input.readLine());
      for (int i = 0; i < m; ++i) {
        nextLine = input.readLine().split("\\s+");
        seed = new Seeds(nextLine[0], nextLine[2], "assets/images/"+nextLine[1]+".png", nextLine[3]);
        HoldableFactory.holdablePool.put(seed.getName(), seed);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Holdable getHoldable(String holdable) {
    return (Holdable)HoldableFactory.holdablePool.get(holdable);
  }
}