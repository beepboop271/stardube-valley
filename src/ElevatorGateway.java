public class ElevatorGateway extends Gateway {
  public ElevatorGateway(int originX, int originY) {
    super(originX, originY, Gateway.OMNIDIRECTIONAL, true);
  }

  public void resetElevatorDestination() {
    this.setDestinationArea(null);
    this.setDestinationGateway(null);
  }
}