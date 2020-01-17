import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GroundTile]
 * A tile for specifically ground, which can be tillable and watered.
 * 2019-12-18
 * @version 0.2
 * @author Kevin Qiao, Joseph Wang
 */
public class GroundTile extends Tile {
  private boolean isTilled;
  private long lastWatered;
  private BufferedImage imageToDisplay;

  //- Different states that the tile can exist in
  private static BufferedImage groundTileImage;
  private static BufferedImage tilledTileImage;
  private static BufferedImage wateredTileImage;

  private static BufferedImage mineGroundImage;

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

  public GroundTile setMineImage() {
    this.imageToDisplay = GroundTile.mineGroundImage;
    return this;
  }

  public void determineImage(long currentDay) {
    if (this.imageToDisplay == GroundTile.mineGroundImage) {
      return;
    }
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

  public static void setGroundTileImages() {
    try {
      GroundTile.groundTileImage = ImageIO.read(new File("assets/images/tiles/ground1.png"));
      GroundTile.tilledTileImage = ImageIO.read(new File("assets/images/tiles/tilled1.png"));
      GroundTile.wateredTileImage = ImageIO.read(new File("assets/images/tiles/watered1.png"));
      GroundTile.mineGroundImage = ImageIO.read(new File("assets/images/tiles/MineGround1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}