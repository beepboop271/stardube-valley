/**
 * [GatewayZone]
 * 2019-12-19
 * A region that takes you to another GatewayZone in another Area
 * @version 0.1
 * @author Kevin Qiao
 */
public class GatewayZone extends Gateway {
  /**
   * [GatewayZone]
   * Constructor for a new GatewayZone.
   * @param originX     The x position of this gateway.
   * @param originY     The y position of this gateway.
   * @param orientation The orientation of this gateway 
   */
  public GatewayZone(int originX, int originY, int orientation) {
    super(originX, originY, orientation, false);
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDestinationArea(Area destinationArea) {
    super.setDestinationArea(destinationArea);
    this.setDestinationGateway(
        this.getDestinationArea()
            .getNeighbourZone(World.getOppositeDirection(this.getOrientation()))
    );
  }
}