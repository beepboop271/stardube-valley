/**
 * [GatewayZone]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */
public class GatewayZone extends Gateway{
  private int orientation;

  public GatewayZone(int originX, int originY, int orientation) {
    super(originX, originY);
    this.orientation = orientation;
  }

  @Override
  public Point toDestinationPoint(Point p, double size) {
    if ((this.orientation == World.NORTH) || (this.orientation == World.SOUTH)) {
      return super.toDestinationPoint(p, size);
    } else {
      p.translate(-this.getOrigin().x, -this.getOrigin().y);
      p.x = Math.copySign(0.5-size, -p.x);
      p.translate(this.getDestinationGateway().getOrigin());
      return p;
    }
  }

  @Override
  public void setDestinationArea(Area destinationArea) {
    super.setDestinationArea(destinationArea);
    this.setDestinationGateway(
        this.getDestinationArea()
            .getNeighbourZone(World.getOppositeDirection(this.orientation))
    );
  }
}