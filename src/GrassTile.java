import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GrassTile]
 * A tile that has to do with grass, which foragables can spawn on.
 * 2019-12-23
 * @version 0.1
 * @author Paula Yuan
 */
public class GrassTile extends Tile {
  private static BufferedImage grassTileImage;

  /**
   * [GrassTile]
   * Constructor for a new GrassTile.
   * @param x  The x position of this tile.
   * @param y  The y position of this tile.
   */
  public GrassTile(int x, int y) {
    super(x, y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return GrassTile.grassTileImage;
  }

  /**
   * [setGrassTileImage]
   * Attempts to load in the grass tile image.
   */
  public static void setGrassTileImage() {
    try {
      GrassTile.grassTileImage = ImageIO.read(new File("assets/images/tiles/grass1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}