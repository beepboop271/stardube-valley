import java.util.ArrayList;

/**
 * [RiverTile]
 * 2019-12-27
 * @version 0.1
 * @author Candice Zhang
 */

public class RiverTile extends WaterTile {
  private static ArrayList<String> fishableFish;

  public RiverTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    RiverTile.fishableFish = new ArrayList<String>();
    RiverTile.fishableFish.add("PopcornFish");
    RiverTile.fishableFish.add("BurgerFish");
    RiverTile.fishableFish.add("BobaFish");
  }
  
  public ArrayList<String> getFishableFish() {
    return RiverTile.fishableFish;
  }
}