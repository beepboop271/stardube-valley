import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [PathTile]
 * A path tile that is used for design only.
 * 2020-1-7
 * @version 0.1
 * @author Joseph Wang
 */

public class PathTile extends Tile {
  private static BufferedImage pathTileImage;

  public PathTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return PathTile.pathTileImage;
  }

  public static void setPathTileImage() {
    try {
      PathTile.pathTileImage = ImageIO.read(new File("assets/images/tiles/RockPath.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}