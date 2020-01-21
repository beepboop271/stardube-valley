/**
 * [ElevatorGateway]
 * A gateway for an elevator, which can change destinations.
 * 2020-1-19
 * @version 0.1
 * @author Kevin Qiao
 */

public class ElevatorGateway extends Gateway {
  /**
   * [ElevatorGateway]
   * Constructor for a new ElevatorGateway.
   * @param originX The x position of this gateway.
   * @param originY The y position of this gateway
   */
  public ElevatorGateway(int originX, int originY) {
    super(originX, originY, Gateway.OMNIDIRECTIONAL, true);
  }

  /**
   * [resetElevatorDestination]
   * Resets this elevator's destination area and gateway.
   */
  public void resetElevatorDestination() {
    this.setDestinationArea(null);
    this.setDestinationGateway(null);
  }
}