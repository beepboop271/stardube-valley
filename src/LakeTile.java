import java.util.ArrayList;

/**
 * [LakeTile]
 * 2019-12-27
 * @version 0.1
 * @author Candice Zhang
 */

public class LakeTile extends WaterTile {
  private static ArrayList<String> fishableFish;

  public LakeTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    LakeTile.fishableFish = new ArrayList<String>();
    LakeTile.fishableFish.add("Carp");
    LakeTile.fishableFish.add("SmallmouthBass");
    LakeTile.fishableFish.add("LargemouthBass");
  }
  
  public ArrayList<String> getFishableFish() {
    return LakeTile.fishableFish;
  }
}