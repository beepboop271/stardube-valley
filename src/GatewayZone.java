/**
 * [GatewayZone]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao, Joseph Wang
 */
public class GatewayZone extends Gateway{

  public GatewayZone(int originX, int originY, int orientation) {
    super(originX, originY, orientation, false);
  }

  @Override
  public Point toDestinationPoint(Point p, double size) {
    if ((this.getOrientation() == World.NORTH) || (this.getOrientation() == World.SOUTH)) {
      p.translate(-this.getOrigin().x, -this.getOrigin().y);
      p.y = Math.copySign(0.5-size, -p.y);
      p.translate(this.getDestinationGateway().getOrigin());
      return p;
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
            .getNeighbourZone(World.getOppositeDirection(this.getOrientation()))
    );
  }
}