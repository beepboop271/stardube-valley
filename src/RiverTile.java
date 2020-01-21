/**
 * [RiverTile]
 * A water tile that is part of a river.
 * 2019-12-29
 * @version 0.3
 * @author Candice Zhang
 */

public class RiverTile extends WaterTile {
  private static String[] fishableFish;

  /**
   * [RiverTile]
   * Constructor for a new RiverTile.
   * @param x The x position of the RiverTile.
   * @param y The y position of the RiverTile.
   */
  public RiverTile(int x, int y) {
    super(x, y);
  }

  /**
   * [setFishableFish]
   * Initializes the fishable fish products for RiverTile.
   */
  public static void setFishableFish() {
    RiverTile.fishableFish = new String[] {"Popcorn-Fish", "Burger-Fish", "Boba-Fish"};
  }

  /**
   * [getFishableFish]
   * Retrieves the fishable fish products for RiverTile.
   * @return String[], the fishable fish products for RiverTile.
   */
  public String[] getFishableFish() {
    return RiverTile.fishableFish;
  }
}