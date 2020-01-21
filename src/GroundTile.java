import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * [GroundTile]
 * A tile for specifically ground, which can be tillable and watered.
 * 2019-12-25
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

  /**
   * [GroundTile]
   * Constructor for a new GroundTile.
   * @param x The x position of this ground tile.
   * @param y The y position of this ground tile.
   */
  public GroundTile(int x, int y) {
    super(x, y);

    this.imageToDisplay = GroundTile.groundTileImage;
  }

  /**
   * [getTilledStatus]
   * Checks to see whether this tile was tilled or not.
   * @return boolean, whether this tile was tilled or not.
   */
  public boolean getTilledStatus() {
    return this.isTilled;
  }

  /**
   * [setTilledStatus]
   * Assigns this tile's tilled status to the provided status.
   * @param status The new tilled status of this tile.
   */
  public void setTilledStatus(boolean status) {
    this.isTilled = status;
  }

  /**
   * [getLastWatered]
   * Retrieves the last date this tile was watered
   * @return long, the day this tile was last watered.
   */
  public long getLastWatered() {
    return this.lastWatered;
  }

  /**
   * [setLastWatered]
   * Sets this tile's last watered date to the specified date.
   * @param day The new watered date for this tile.
   */
  public void setLastWatered(long day) {
    this.lastWatered = day;
  }

  /**
   * [setMineImage]
   * Sets the image to display to the ground mine image.
   * @return GroundTile, this.
   */
  public GroundTile setMineImage() {
    this.imageToDisplay = GroundTile.mineGroundImage;
    return this;
  }

  /**
   * [determineImage]
   * Using the current day, determines what image this ground tile
   * should display.
   * @author Joseph Wang, Kevin Qiao
   * @param currentDay the day to be used during calculations.
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.imageToDisplay;
  }

  /**
   * [setGroundTileImages]
   * Attemps to load in all the ground tile images.
   * @author Joseph Wang, Kevin Qiao
   */
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