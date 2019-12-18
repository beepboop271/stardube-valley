import java.awt.Point;

/**
 * [GatewayZone]
 * 2019-12-18
 * @version 0.1
 * @author Kevin Qiao
 */
public class GatewayZone {
  private Area destinationArea;
  private GatewayZone destinationZone;
  private int originX, originY;

  public GatewayZone(int originX, int originY) {
    this.originX = originX;
    this.originY = originY;
  }

  public GatewayZone(Area destinationArea, GatewayZone destinationZone,
                     int originX, int originY) {
    this(originX, originY);
    this.destinationArea = destinationArea;
    this.destinationZone = destinationZone;
  }

  public Point getDestinationPoint(int x, int y) {
    Point p = new Point(x, y);
    p.translate(-(int)this.originX, -(int)this.originY);
    p.translate(this.destinationZone.getX(), this.destinationZone.getY());
    return p;
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

  public int getX() {
    return this.originX;
  }

  public int getY() {
    return this.originY;
  }
}