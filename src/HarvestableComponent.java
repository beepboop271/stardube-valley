public class HarvestableComponent extends TileComponent {
  String requiredTool;
  int hardness;
  
  public HarvestableComponent(String name, String imagePath,
                              String requiredTool, int hardness,
                              int numProducts) {
    super(name, imagePath, numProducts);
    this.requiredTool = requiredTool;
    this.hardness = hardness;
  }
}