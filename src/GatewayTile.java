
/**
 * [GatewayTile]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public class GatewayTile extends Tile {
  private Area destinationArea;

  public GatewayTile(int x, int y, Area destinationArea) {
    super(x, y);
    this.destinationArea = destinationArea;
  }
}