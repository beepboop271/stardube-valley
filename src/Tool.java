import java.io.IOException;

/**
 * [Tool]
 * A class for any kind of tool that can be used.
 * 2019-12-23
 * @version 0.2
 * @author Kevin Qiao, Joseph Wang
 */

public abstract class Tool extends Useable {
  private int energyCost;
  public Tool(String name, String description, 
              String imagePath, int energyCost) throws IOException {
    super(name, description, imagePath);

    this.energyCost = energyCost;
  }

  public abstract Point[] getUseLocation(Point selectedTile);

  /**
   * [getEnergyCost]
   * Retrieves how much energy it takes to use this tool.
   * @return int, this tool's energy cost to use.
   */
  public int getEnergyCost() {
    return this.energyCost;
  }
}