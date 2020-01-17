import java.io.IOException;

public class IntrinsicHarvestableComponent extends CollectableComponent implements Harvestable {
  private final String requiredTool;
  private final int hardness;

  public IntrinsicHarvestableComponent(String name, 
                                       String imagePath,
                                       String requiredTool,
                                       int hardness,
                                       int numProducts,
                                       int[] offsets) throws IOException {
    super(name, imagePath, numProducts, offsets);
    this.requiredTool = requiredTool;
    this.hardness = hardness;
  }

  public int getHardness() {
    return this.hardness;
  }

  @Override
  public String getRequiredTool() {
    return this.requiredTool;
  }
}