/**
 * [PondTile]
 * A water tile that is part of a pond.
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class PondTile extends WaterTile {
  private static String[] fishableFish;

  /**
   * [PondTile]
   * Constructor for a new PondTile.
   * @param x  int, the x position of the PondTile.
   * @param y  int, the y position of the PondTile.
   */
  public PondTile(int x, int y) {
    super(x, y);
  }

  /**
   * [setFishableFish]
   * Initializes the fishable fish products for PondTile.
   */
  public static void setFishableFish() {
    PondTile.fishableFish = new String[0];
  }
  
  /**
   * [getFishableFish]
   * Retrieves the fishable fish products for PondTile.
   * @return String[], the fishable fish products for PondTile.
   */
  public String[] getFishableFish() {
    return PondTile.fishableFish;
  }
}