/**
 * [OceanTile]
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class OceanTile extends WaterTile {
  private static String[] fishableFish;

  public OceanTile(int x, int y) {
    super(x, y);
  }

  public static void setFishableFish() {
    OceanTile.fishableFish = new String[] {"Tuna", "Anchovy", "Sardine"};
  }
  
  public String[] getFishableFish() {
    return OceanTile.fishableFish;
  }
}