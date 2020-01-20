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

  /**
   * [Tool]
   * Constructor for a new Tool.
   * @author Kevin Qiao, Joseph Wang
   * @param name          The name of this tool.
   * @param description   The description of this tool.
   * @param imagePath     The path to this tool's images.
   * @param energyCost    How much energy this tool needs.
   * @throws IOException
   */
  public Tool(String name, String description, 
              String imagePath, int energyCost) throws IOException {
    super(name, description, imagePath);

    this.energyCost = energyCost;
  }

  /**
   * [getUseLocation]
   * Retrieves all the points where this tool is used on.
   * @param selectedTile The tile that was selected when this tool was used.
   * @return Point[], a Point array with all the points this tool was used on.
   */
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