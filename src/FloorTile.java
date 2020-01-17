import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//TODO: combine this with path to do smth like decoratedTile

public class FloorTile extends Tile {
  private static BufferedImage floorTileImage;

  public FloorTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return FloorTile.floorTileImage;
  }

  public static void setFloorTileImage() {
    try {
      FloorTile.floorTileImage = ImageIO.read(new File("assets/images/tiles/FloorTile.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}