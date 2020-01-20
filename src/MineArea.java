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
   * @param name, the String name of the area
   * @param width, the int width of the area
   * @param height, the int height of the area
   * @param startingGateway, the Point representing where the starting gateway should be
   * @param levelExitGateway, the Point representing where the exit gateway for a level should be
   * @param elevatorGateway, the Point representing where the elevator should be
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
   * Loads a certain level of the mine.
   * @param level, an int representing the level you want to load.
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

  @Override
  /**
   * {@inheritDoc}
   */
  public void doDayEndActions() {
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
   * @return Gateway the level exit gateway
   */
  public Gateway getLevelExitGateway() {
    return this.levelExitGateway;
  }

  /**
   * [getElevatorPosition]
   * Gets the position of the elevator
   * @return the Point origin of the elevator
   */
  public Point getElevatorPosition() {
    return this.elevator.getOrigin();
  }

  /**
   * [setElevatorDestination]
   * Sets the level that the elevator is going to
   * @param level, the level you want to go to
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
   * @return boolean true or false depending on the floor's elevator accessibility
   */
  public boolean hasElevatorFloorUnlocked(int floor) {
    return ((floor/5) <= this.elevatorLevelsUnlocked);
  }
}