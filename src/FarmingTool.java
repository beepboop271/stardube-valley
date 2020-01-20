import java.io.IOException;

public class FarmingTool extends Tool {
  public FarmingTool(String name, String description, String imagePath, String type) throws IOException {
    super(name, description, imagePath, 2, type);
  }
//TODO: finish farming tools
  @Override //temp, stolen from UtilityTool
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}