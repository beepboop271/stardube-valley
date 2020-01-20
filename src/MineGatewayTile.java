import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * [MineGatewayTile]
 * A gateway tile for the mine
 * 2019-12-30
 * @version 0.1
 * @author Kevin Qiao
 */
public class MineGatewayTile extends Tile implements NotWalkable {
  public static final int DOWNWARDS_LADDER = 1;
  private static BufferedImage downwardsLadderImage;
  public static final int UPWARDS_LADDER = 2;
  private static BufferedImage upwardsLadderImage;
  public static final int ELEVATOR = 3;
  private static BufferedImage elevatorImage;
  private BufferedImage image;

  /**
   * [MineGatewayTile]
   * Constructor for a new MineGatewayTile.
   * @param x, the x int position of the tile
   * @param y, the y int position of the tile
   * @param image, the int representing what image should be drawn for the tile
   */
  public MineGatewayTile(int x, int y, int image) {
    super(x, y);
    switch (image) {
      case MineGatewayTile.DOWNWARDS_LADDER:
        this.image = MineGatewayTile.downwardsLadderImage;
        break;
      case MineGatewayTile.UPWARDS_LADDER:
        this.image = MineGatewayTile.upwardsLadderImage;
        break;
      case MineGatewayTile.ELEVATOR:
        this.image = MineGatewayTile.elevatorImage;
        break;
      default:
        throw new IllegalArgumentException("invalid image type");
    }
  }

  @Override
  /**
   * {@inheritDocs}
   */
  public BufferedImage getImage() {
    return this.image;
  }

  /**
   * [setLadderImage]
   * Sets the image for the ladder tile.
   */
  public static void setLadderImage() {
    try {
      MineGatewayTile.downwardsLadderImage = ImageIO.read(new File("assets/images/tiles/downwardsLadder.png"));
      MineGatewayTile.upwardsLadderImage = ImageIO.read(new File("assets/images/tiles/upwardsLadder.png"));
      MineGatewayTile.elevatorImage = ImageIO.read(new File("assets/images/tiles/elevator.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}