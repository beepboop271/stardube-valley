/**
 * HarvestableComponent
 * 2019-12-21
 * @version 0.2
 * @author Kevin Qiao, Paula Yuan
 */
public class HarvestableComponent extends TileComponent implements Harvestable {
  private String requiredTool;
  private int hardness;
  
  // methods
  public void setHardness(int hardness) {
    this.hardness = hardness;
  }

  public int getHardness() {
    return this.hardness;
  }

  public void setRequiredTool(String tool) {
    this.requiredTool = tool;
  }

  public String getRequiredTool() {
    return this.requiredTool;    
  }

  // constructors
  public HarvestableComponent(String name, String imagePath,
                              String requiredTool, int hardness,
                              int numProducts) {
    super(name, imagePath, numProducts);
    this.requiredTool = requiredTool;
    this.hardness = hardness;
  }
}