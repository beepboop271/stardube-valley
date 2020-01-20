import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * [WaterTile]
 * A tile that has water and contains fishable products.
 * 2019-12-26
 * @version 0.2
 * @author Candice Zhang
 */

public abstract class WaterTile extends Tile implements NotWalkable {
  private static BufferedImage waterTileImage;
  private static String[] fishableTrash;

  /**
   * [WaterTile]
   * Constructor for a new WaterTile.
   * @param x  int, the x position of the WaterTile.
   * @param y  int, the y position of the WaterTile.
   */
  public WaterTile(int x, int y) {
    super(x, y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return WaterTile.waterTileImage;
  }
  
  /**
   * [setWaterTileImage]
   * Initializes the image for WaterTile.
   */
  public static void setWaterTileImage() {
    try {
      WaterTile.waterTileImage = ImageIO.read(new File("assets/images/tiles/water1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * [setFishableTrash]
   * Initializes the fishable trash products for WaterTile.
   */
  public static void setFishableTrash() {
    WaterTile.fishableTrash = new String[] {"Joseph's-Compsci-Mark", "Purple-Algae", "Kevin's-Ethics-Assignment", "Chair-NULL", "MakBooc"};
  }

  /**
   * [getFishableTrash]
   * Retrieves the fishable trash products for WaterTile.
   * @return String[], the fishable trash products for WaterTile.
   */
  public static String[] getFishableTrash() {
    return WaterTile.fishableTrash;
  }

  /**
   * [getFishableFish]
   * Retrieves the fishable fish products for the water tile.
   * @return String[], the fishable fish products for the water tile.
   */
  abstract public String[] getFishableFish();
}