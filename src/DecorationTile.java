import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [DecorationTile]
 * A set of tiles that are only used for decoration.
 * 2020-1-7
 * @version 0.2
 * @author Joseph Wang
 */

public class DecorationTile extends Tile {
  private static BufferedImage pathTileImage;
  private static BufferedImage floorTileImage;
  private static BufferedImage plankTileImage;

  private BufferedImage tileImage;

  /**
   * [DecorationTile]
   * Constructor for a new decoration tile. Needs the position of the tile and
   * what type of tile it is.
   * @param x        The x position of this tile.
   * @param y        The y position of this tile.
   * @param tileType A char used to identify which type of tile this is.
   */
  public DecorationTile(int x, int y, char tileType) {
    super(x, y);

    switch(tileType) {
      case 'b':
        this.tileImage = DecorationTile.pathTileImage;
        break;
      case 'f':
        this.tileImage = DecorationTile.floorTileImage;
        break;
      case '-':
        this.tileImage = DecorationTile.plankTileImage;
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.tileImage;
  }

  /**
   * [setTileImages]
   * Attempts to load in all the images needed for decorative tiles.
   */
  public static void setTileImages() {
    try {
      DecorationTile.pathTileImage = ImageIO.read(new File("assets/images/tiles/RockPath.png"));
      DecorationTile.floorTileImage = ImageIO.read(new File("assets/images/tiles/FloorTile.png"));
      DecorationTile.plankTileImage = ImageIO.read(new File("assets/images/tiles/PlankTile.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}