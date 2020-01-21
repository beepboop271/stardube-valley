import java.io.IOException;

/**
 * [IntrinsicHarvestableComponent]
 * A class that holds shared data between components that can be harvested (eg. rocks, trees)
 * 2019-12-27
 * @version 0.2
 * @author Kevin Qiao, Joseph Wang
 */
public class IntrinsicHarvestableComponent extends CollectableComponent implements Harvestable {
  private final String requiredTool;
  private final int hardness;

  /**
   * [IntrinsicHarvestableComponent]
   * Constructor for a new IntrinsicHarvestableComponent.
   * @param name         The name of this component.
   * @param imagePath    The path to the images related to this component.
   * @param requiredTool The tool needed in order to harvest this component.
   * @param hardness     The total amount of damage needed to harvest this component.
   * @param numProducts  The total amount of different products that can be dropped.
   * @param offsets      The image offsets (in tiles) that should be considered when drawing.
   * @throws IOException
   */
  public IntrinsicHarvestableComponent(String name, String imagePath, String requiredTool,
                                       int hardness, int numProducts,
                                       double[] offsets) throws IOException {
    super(name, imagePath, numProducts, offsets);
    this.requiredTool = requiredTool;
    this.hardness = hardness;
  }

  /**
   * [getHardness]
   * Retrieves the total amount of damage needed to break this harvestable.
   * @return int, the total amount of damage needed.
   */
  public int getHardness() {
    return this.hardness;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRequiredTool() {
    return this.requiredTool;
  }
}