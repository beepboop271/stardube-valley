import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

/**
 * [WaterTile]
 * 2019-12-26
 * @version 0.1
 * @author Candice Zhang
 */

public abstract class WaterTile extends Tile {
  private static BufferedImage waterTileImage;
  private static ArrayList<String> fishableTrash;

  public WaterTile(int x, int y) {
    super(x, y);
  }

  @Override
  public BufferedImage getImage() {
    return WaterTile.waterTileImage;
  }
  
  public static void setWaterTileImage() {
    try {
      WaterTile.waterTileImage = ImageIO.read(new File("assets/images/tiles/ground1.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void setFishableTrash() {
    WaterTile.fishableTrash = new ArrayList<String>();
    WaterTile.fishableTrash.add("Joseph'sCompsciMark");
    WaterTile.fishableTrash.add("PurpleAlgae");
    WaterTile.fishableTrash.add("EthicsAssignment");
    WaterTile.fishableTrash.add("ChairNULL");
    WaterTile.fishableTrash.add("Makbooc");
  }

  public static ArrayList<String> getFishableTrash() {
    return WaterTile.fishableTrash;
  }

  abstract public ArrayList<String> getFishableFish();
}