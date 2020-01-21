/**
 * [OceanTile]
 * A water tile that is part of an ocean.
 * 2019-12-29
 * @version 0.2
 * @author Candice Zhang
 */

public class OceanTile extends WaterTile {
  private static String[] fishableFish;

  /**
   * [OceanTile]
   * Constructor for a new OceanTile.
   * @param x  the x position of the OceanTile.
   * @param y  the y position of the OceanTile.
   */
  public OceanTile(int x, int y) {
    super(x, y);
  }

  /**
   * [setFishableFish]
   * Initializes the fishable fish products for OceanTile.
   */
  public static void setFishableFish() {
    OceanTile.fishableFish = new String[] {"Tuna", "Anchovy", "Sardine"};
  }
  
  /**
   * [getFishableFish]
   * Retrieves the fishable fish products for OceanTile.
   * @return String[], the fishable fish products for OceanTile.
   */
  public String[] getFishableFish() {
    return OceanTile.fishableFish;
  }
}