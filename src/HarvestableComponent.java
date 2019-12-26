import java.io.IOException;

/**
 * HarvestableComponent 2019-12-21
 * 
 * @version 0.2
 * @author Kevin Qiao, Paula Yuan
 */
public class HarvestableComponent extends TileComponent implements Harvestable {
  private final String requiredTool;
  private final int hardness;

  public HarvestableComponent(String name, String imagePath,
                              String requiredTool, int hardness,
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