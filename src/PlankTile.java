import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [PlankTile]
 * 2020-01-18
 * @version 0.1
 * @author Paula Yuan
 */
public class PlankTile extends Tile {
  private static BufferedImage plankTileImage;

  public PlankTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return PlankTile.plankTileImage;
  }

  public static void setPlankTileImage() {
    try {
      PlankTile.plankTileImage = ImageIO.read(new File("assets/images/tiles/PlankTile.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}