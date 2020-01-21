import java.io.IOException;

/**
 * [UtilityTool]
 * A class for any utility tool, ie. any tool that uses the 
 * current selected tile as its use location.
 * 2019-12-23
 * @version 0.4
 * @author Kevin Qiao, Joseph Wang
 */

public class UtilityTool extends Tool {
  private int effectiveness;
  /**
   * [UtilityTool]
   * Constructor for a new UtilityTool.
   * @param name          The name of this tool.
   * @param description   The description of this tool.
   * @param imagePath     The path to this tool's image.
   * @param energyCost    How much energy it takes to use this tool.
   * @param effectiveness How good this tool is at breaking things.
   * @throws IOException
   */
  public UtilityTool(String name, String description, 
                     String imagePath, int energyCost,
                     int effectiveness, String type) throws IOException {
    super(name, description, imagePath, energyCost, type);
    this.effectiveness = effectiveness;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }

  /**
   * [getEffectiveness]
   * Retrieves how strong this tool is (ie. how much "damage" is
   * inflicted each hit of this tool).
   * @return int, this tool's effectiveness.
   */
  public int getEffectiveness() {
    return this.effectiveness;
  }
}