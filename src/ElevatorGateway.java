/**
 * [ElevatorGateway]
 * 2020-1-19
 * @version 0.1
 * @author Kevin Qiao
 */

public class ElevatorGateway extends Gateway { //TODO: JAVADOCS
  public ElevatorGateway(int originX, int originY) {
    super(originX, originY, Gateway.OMNIDIRECTIONAL, true);
  }

  public void resetElevatorDestination() {
    this.setDestinationArea(null);
    this.setDestinationGateway(null);
  }
}