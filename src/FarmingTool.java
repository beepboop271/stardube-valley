import java.io.IOException;

public class FarmingTool extends UtilityTool {
  public FarmingTool(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath, 2);
  }
//TODO: finish farming tools
  @Override //temp, stolen from UtilityTool
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }
}