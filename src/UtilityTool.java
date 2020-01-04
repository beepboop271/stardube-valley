import java.io.IOException;

public class UtilityTool extends Tool {
  private int effectiveness;

  public UtilityTool(String name, String description, String imagePath) throws IOException {
    super(name, description, imagePath);
    this.effectiveness = 1;
  }

  @Override
  public Point[] getUseLocation(Point selectedTile) {
    Point[] returnValue = {selectedTile};
    return returnValue;
  }

  public int getEffectiveness() {
    return this.effectiveness;
  }
}