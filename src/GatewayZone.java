/**
 * [GatewayZone]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class GatewayZone {
  private Area destinationArea;
  private GatewayZone destinationZone;
  private Point origin;
  private boolean isHorizontal;

  public GatewayZone(int originX, int originY, boolean isHorizontal) {
    this.origin = new Point(originX, originY);
    this.isHorizontal = isHorizontal;
  }

  public GatewayZone(Area destinationArea, GatewayZone destinationZone,
                     int originX, int originY, boolean isHorizontal) {
    this(originX, originY, isHorizontal);
    this.destinationArea = destinationArea;
    this.destinationZone = destinationZone;
  }

  public Point toDestinationPoint(Point p, double size) {
    p.translate(-this.origin.x, -this.origin.y);
    if (this.isHorizontal) {
      p.y = Math.copySign(0.5-size, -p.y);
    } else {
      p.x = Math.copySign(0.5-size, -p.x);
    }
    p.translate(this.destinationZone.origin.x, this.destinationZone.origin.y);
    return p;
  }

  public Area getDestinationArea() {
    return this.destinationArea;
  }

  public void setDestinationArea(Area destinationArea, int direction) {
    this.destinationArea = destinationArea;
    if (direction == World.NORTH) {
      this.destinationZone = this.destinationArea.getNeighbourZone(World.SOUTH);
    } else if (direction == World.SOUTH) {
      this.destinationZone = this.destinationArea.getNeighbourZone(World.NORTH);
    } else if (direction == World.WEST) {
      this.destinationZone = this.destinationArea.getNeighbourZone(World.EAST);
    } else if (direction == World.EAST) {
      this.destinationZone = this.destinationArea.getNeighbourZone(World.WEST);
    }
  }
}