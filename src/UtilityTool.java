public class UtilityTool extends Tool {
  public UtilityTool(String name, String description, String imagePath) {
    super(name, description, imagePath);
  }

  @Override
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}