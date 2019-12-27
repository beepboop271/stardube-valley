/**
 * [FishingRod]
 * 2019-12-22
 * @version 0.1
 * @author Candice Zhang
 */
public class FishingRod extends Tool {
  FishingRod(String name, String description, String imagePath) {
    super(name, description, imagePath);
  }

  @Override
  public Point[] getUseLocation(Point tileUsed) {
    return null;
  }
}