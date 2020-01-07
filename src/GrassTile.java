import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GrassTile]
 * 2019-12-23
 * @version 0.1
 * @author Paula Yuan
 */
public class GrassTile extends Tile {
  private static BufferedImage grassTileImage;

  public GrassTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return GrassTile.grassTileImage;
  }

  public static void setGrassTileImage() {
    try {
      GrassTile.grassTileImage = ImageIO.read(new File("assets/images/tiles/grass1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}