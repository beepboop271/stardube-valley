import java.util.ArrayList;

/**
 * [OceanTile]
 * 2019-12-27
 * @version 0.1
 * @author Candice Zhang
 */

public class OceanTile extends WaterTile {
  private static ArrayList<String> fishableFish;

  public OceanTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    OceanTile.fishableFish = new ArrayList<String>();
    OceanTile.fishableFish.add("Tuna");
    OceanTile.fishableFish.add("Anchovy");
    OceanTile.fishableFish.add("Sardine");
  }
  
  public ArrayList<String> getFishableFish() {
    return OceanTile.fishableFish;
  }
}