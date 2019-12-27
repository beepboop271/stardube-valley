import java.io.IOException;

public class IntrinsicHarvestableComponent extends IntrinsicTileComponent implements Harvestable {
  private final String requiredTool;
  private final int hardness;

  public IntrinsicHarvestableComponent(String name, 
                                       String imagePath,
                                       String requiredTool,
                                       int hardness,
                                       int numProducts) throws IOException {
    super(name, imagePath, numProducts);
    this.requiredTool = requiredTool;
    this.hardness = hardness;
  }

  public int getHardness() {
    return this.hardness;
  }

  public String getRequiredTool() {
    return this.requiredTool;
  }
}