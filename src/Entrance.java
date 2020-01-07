public class Entrance extends GatewayZone {
  public Entrance(int originX, int originY) {
    super(originX, originY);
  }

  public Entrance(Area destinationArea, GatewayZone destinationZone,
                     int originX, int originY) {
    super(destinationArea, destinationZone, originX, originY);
  }

  public void initializeDestination(Area destinationArea, int direction) {
    this.setDestinationArea(destinationArea);

  }
}