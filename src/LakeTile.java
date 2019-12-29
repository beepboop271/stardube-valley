/**
 * [LakeTile]
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class LakeTile extends WaterTile {
  private static String[] fishableFish;

  public LakeTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    LakeTile.fishableFish = new String[] {"Carp", "Smallmouth-Bass", "Largemouth-Bass"};
  }
  
  public String[] getFishableFish() {
    return LakeTile.fishableFish;
  }
}