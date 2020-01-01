/**
 * [RiverTile]
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class RiverTile extends WaterTile {
  private static String[] fishableFish;

  public RiverTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    RiverTile.fishableFish = new String[] {"Popcorn-Fish", "Burger-Fish", "Boba-Fish"};
  }
  
  public String[] getFishableFish() {
    return RiverTile.fishableFish;
  }
}