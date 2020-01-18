import java.io.IOException;

/**
 * [UtilityTool]
 * A class for any utility tool, ie. any tool that uses the current selected tile as
 * its use location.
 * 2019-12-23
 * @version 0.3
 * @author Kevin Qiao, Joseph Wang
 */

public class UtilityTool extends Tool {
  private int effectiveness;

  public UtilityTool(String name, String description, 
                     String imagePath, int energyCost) throws IOException {
    super(name, description, imagePath, energyCost);
    this.effectiveness = 1;
  }

  @Override
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }

  public int getEffectiveness() {
    return this.effectiveness;
  }
}