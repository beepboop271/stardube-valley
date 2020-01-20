/**
 * [LakeTile]
 * A water tile that is part of a lake.
 * 2019-12-27
 * @version 0.2
 * @author Candice Zhang
 */

public class LakeTile extends WaterTile {
  private static String[] fishableFish;

  /**
   * [LakeTile]
   * Constructor for a new LakeTile.
   * @param x  int, the x position of the LakeTile.
   * @param y  int, the y position of the LakeTile.
   */
  public LakeTile(int x, int y) {
    super(x, y);
  }

  /**
   * [setFishableFish]
   * Initializes the fishable fish products for LakeTile.
   */
  public static void setFishableFish() {
    LakeTile.fishableFish = new String[] {"Carp", "Smallmouth-Bass", "Largemouth-Bass"};
  }
  
  /**
   * [getFishableFish]
   * Retrieves the fishable fish products for LakeTile.
   * @return String[], the fishable fish products for LakeTile.
   */
  public String[] getFishableFish() {
    return LakeTile.fishableFish;
  }
}