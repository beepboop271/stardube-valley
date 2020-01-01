import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GroundTile]
 * 2019-12-18
 * @version 0.1
 * @author Kevin Qiao
 */
public class GroundTile extends Tile {
  private boolean isTilled;
  private int lastWatered;

  private static BufferedImage groundTileImage;

  public GroundTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return GroundTile.groundTileImage;
  }

  public static void setGroundTileImage() {
    try {
      GroundTile.groundTileImage = ImageIO.read(new File("assets/images/tiles/ground1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}