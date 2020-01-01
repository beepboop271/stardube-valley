/**
 * [PondTile]
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class PondTile extends WaterTile {
  private static String[] fishableFish;

  public PondTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    PondTile.fishableFish = new String[0];
  }
  
  public String[] getFishableFish() {
    return PondTile.fishableFish;
  }
}