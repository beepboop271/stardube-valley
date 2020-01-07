/**
 * [GatewayZone]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class GatewayZone {
  private Area destinationArea;
  private GatewayZone destinationZone;
  private Point origin;

  public GatewayZone(int originX, int originY) {
    this.origin = new Point(originX, originY);
  }

  public GatewayZone(Area destinationArea, GatewayZone destinationZone,
                     int originX, int originY) {
    this(originX, originY);
    this.destinationArea = destinationArea;
    this.destinationZone = destinationZone;
  }

  public abstract Point toDestinationPoint(Point p, double size);

  public Point getOrigin() {
    return this.origin;
  }

  public Area getDestinationArea() {
    return this.destinationArea;
  }

  public void setDestinationArea(Area destinationArea) {
    this.destinationArea = destinationArea;
  }

  public GatewayZone getDestinationZone() {
    return this.destinationZone;
  }

  public void setDestinationZone(GatewayZone destinationZone) {
    this.destinationZone = destinationZone;
  }

  public abstract void initializeDestination(Area destinationArea, int direction);
}