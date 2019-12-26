import java.io.IOException;

/**
 * [FishingRod]
 * 2019-12-22
 * @version 0.1
 * @author Candice Zhang
 */
public class FishingRod extends Tool {
  public FishingRod() throws IOException {
    super("Fishing Rod", "best tool:)", null);
  }

  @Override
  public Point[] getUseLocation(Point selectedTile) {
    return null;
  }
}