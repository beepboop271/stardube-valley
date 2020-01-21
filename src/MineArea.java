import java.util.Iterator;

/**
 * [MineArea]
 * A mine area
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public class MineArea extends Area {
  public static final int NUM_LEVELS = 60;

  private MineLevel[] levels;
  private Gateway levelExitGateway;
  private Gateway startingGateway;
  private ElevatorGateway elevator;
  private int elevatorLevelsUnlocked;

  /**
   * [MineArea]
   * Constructor for a new MineArea.
   * @param name             The name of the area
   * @param width            The width of the area
   * @param height           The height of the area
   * @param startingGateway  The position of the starting gateway (floor 0 entrance)
   * @param levelExitGateway The position of the exit gateway (exit of all floors)
   * @param elevatorGateway  The position of the elevator
   */
  public MineArea(String name,
                  int width, int height,
                  Point startingGateway,
                  Point levelExitGateway,
                  Point elevatorGateway) {
    super(name, width, height);
    this.levels = new MineLevel[MineArea.NUM_LEVELS];

    this.elevatorLevelsUnlocked = 0;

    this.startingGateway = new Gateway((int)(startingGateway.x),
                                       (int)(startingGateway.y),
                                       Gateway.OMNIDIRECTIONAL,
                                       true);
    this.addGateway(this.startingGateway);
    this.levelExitGateway = new Gateway((int)(levelExitGateway.x),
                                        (int)(levelExitGateway.y),
                                        Gateway.ONE_WAY, true);
    this.elevator = new ElevatorGateway((int)(elevatorGateway.x),
                                        (int)(elevatorGateway.y));
    this.addGateway(elevator);
  }

  /**
   * [loadLevel]
   * Loads a certain level of the mine, and sets up the connection
   * between the previous level and the new level.
   * @param level The floor number to be loaded.
   */
  public void loadLevel(int level) {
    if (level == 0) {
      return;
    }
    if (this.levels[level] == null) {
      this.elevatorLevelsUnlocked = Math.max(this.elevatorLevelsUnlocked,
                                             (int)((level-1)/5.0));
  
      this.levels[level] = new MineLevel.Builder(level).buildLevel(this);
    }
    Iterator<Gateway> gateways = this.levels[level-1].getGateways();
    Gateway nextGateway;
    while (gateways.hasNext()) {
      nextGateway = gateways.next();
      if (nextGateway.getDestinationGateway() == null) {
        nextGateway.setDestinationArea(this.levels[level]);
        nextGateway.setDestinationGateway(this.levels[level].getEntranceGateway());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doDayEndActions() {
    // reset all levels
    this.levels[0] = new MineLevel.Builder(0).buildLevel(this);
    this.startingGateway.setDestinationArea(this.levels[0]);
    this.startingGateway.setDestinationGateway(this.levels[0].getEntranceGateway());
    
    for (int i = 1; i < MineArea.NUM_LEVELS; ++i) {
      this.levels[i] = null;
    }
  }

  /**
   * [getLevelExitGateway]
   * returns the Gateway representing the exit gateway of the level
   * @return Gateway, the level exit gateway
   */
  public Gateway getLevelExitGateway() {
    return this.levelExitGateway;
  }

  /**
   * [getElevatorPosition]
   * Gets the position of the elevator
   * @return Point, the position of the elevator
   */
  public Point getElevatorPosition() {
    return this.elevator.getOrigin();
  }

  /**
   * [setElevatorDestination]
   * Sets the level that the elevator is going to.
   * @param level the destination floor number
   */ 
  public void setElevatorDestination(int level) {
    if (this.levels[level] == null) {
      this.levels[level] = new MineLevel.Builder(level).buildLevel(this);
    }
    this.elevator.setDestinationArea(this.levels[level]);
    this.elevator.setDestinationGateway(this.levels[level].getEntranceGateway());
  }

  /**
   * [hasElevatorFloorUnlocked]
   * Gets whether a certain floor is accessible by the elevator
   * @return boolean, true if the player has unlocked the elevator at
   *         that floor, false otherwise
   */
  public boolean hasElevatorFloorUnlocked(int floor) {
    return ((floor/5) <= this.elevatorLevelsUnlocked);
  }
}