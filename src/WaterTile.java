import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * [WaterTile]
 * 2019-12-26
 * @version 0.2
 * @author Candice Zhang
 */

public abstract class WaterTile extends Tile {
  private static BufferedImage waterTileImage;
  private static String[] fishableTrash;

  public WaterTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return WaterTile.waterTileImage;
  }
  
  public static void setWaterTileImage() {
    try {
      WaterTile.waterTileImage = ImageIO.read(new File("assets/images/tiles/water1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void setFishableTrash() {
    WaterTile.fishableTrash = new String[] {"Joseph's-Compsci-Mark", "Purple-Algae", "Ethics-Assignment", "Chair-NULL", "MakBooc"};
  }

  public static String[] getFishableTrash() {
    return WaterTile.fishableTrash;
  }

  abstract public String[] getFishableFish();
}