import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GroundTile]
 * 2019-12-18
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */
public class GroundTile extends Tile {
  private boolean isTilled;
  private long lastWatered;
  private BufferedImage imageToDisplay;

  private static BufferedImage groundTileImage;
  private static BufferedImage tilledTileImage;
  private static BufferedImage wateredTileImage;

  public GroundTile(int x, int y) {
    super(x, y);

    this.imageToDisplay = GroundTile.groundTileImage;
  }

  public boolean getTilledStatus() {
    return this.isTilled;
  }

  public void setTilledStatus(boolean status) {
    this.isTilled = status;
  }

  public long getLastWatered() {
    return this.lastWatered;
  }

  public void setLastWatered(long day) {
    this.lastWatered = day;
  }

  public void determineImage(long currentDay) {
    if (this.isTilled) {
      if (this.lastWatered == currentDay) {
        this.imageToDisplay = GroundTile.wateredTileImage;
      } else {
        this.imageToDisplay = GroundTile.tilledTileImage;
      }
    } else {
      this.imageToDisplay = GroundTile.groundTileImage;
    }
  }

  @Override
  public BufferedImage getImage() {
    return this.imageToDisplay;
  }

  public static void setGroundTileImage() {
    try {
      GroundTile.groundTileImage = ImageIO.read(new File("assets/images/tiles/ground1.png"));
      GroundTile.tilledTileImage = ImageIO.read(new File("assets/images/tiles/tilled1.png"));
      GroundTile.wateredTileImage = ImageIO.read(new File("assets/images/tiles/watered1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}