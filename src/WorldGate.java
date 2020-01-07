public class WorldGate extends GatewayZone {
  boolean isHorizontal;

  public WorldGate(int originX, int originY, boolean isHorizontal) {
    super(originX, originY);
    this.isHorizontal = isHorizontal;
  }

  public WorldGate(Area destinationArea, GatewayZone destinationZone,
                     int originX, int originY, boolean isHorizontal) {
    super(destinationArea, destinationZone, originX, originY);
    this.isHorizontal = isHorizontal;
  }

  @Override
  public Point toDestinationPoint(Point p, double size) {
    p.translate(-this.getOrigin().x, -this.getOrigin().y);
    if (this.isHorizontal) {
      p.y = Math.copySign(0.5-size, -p.y);
    } else {
      p.x = Math.copySign(0.5-size, -p.x);
    }
    p.translate(this.getDestinationZone().getOrigin().x, 
                this.getDestinationZone().getOrigin().y);
    return p;
  }

  @Override
  public void initializeDestination(Area destinationArea, int direction) {
    this.setDestinationArea(destinationArea);
    if (direction == World.NORTH) {
      this.setDestinationZone(this.getDestinationArea().getNeighbourZone(World.SOUTH));
    } else if (direction == World.SOUTH) {
      this.setDestinationZone(this.getDestinationArea().getNeighbourZone(World.NORTH));
    } else if (direction == World.WEST) {
      this.setDestinationZone(this.getDestinationArea().getNeighbourZone(World.EAST));
    } else if (direction == World.EAST) {
      this.setDestinationZone(this.getDestinationArea().getNeighbourZone(World.WEST));
    }
  }
}