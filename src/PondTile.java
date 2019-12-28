import java.util.ArrayList;

/**
 * [PondTile]
 * 2019-12-27
 * @version 0.1
 * @author Candice Zhang
 */

public class PondTile extends WaterTile {
  private static ArrayList<String> fishableFish;

  public PondTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    PondTile.fishableFish = new ArrayList<String>();
  }
  
  public ArrayList<String> getFishableFish() {
    return PondTile.fishableFish;
  }
}