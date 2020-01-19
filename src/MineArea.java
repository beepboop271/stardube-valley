import java.util.Iterator;

public class MineArea extends Area {
  public static final int NUM_LEVELS = 60;

  private MineLevel[] levels;
  private Gateway levelExitGateway;
  private Gateway startingGateway;
  private ElevatorGateway elevator;
  private int elevatorLevelsUnlocked;

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

  public void loadLevel(int level) {
    if (level == 0) {
      return;
    }
    this.elevatorLevelsUnlocked = Math.max(this.elevatorLevelsUnlocked,
                                           (int)(level-1/5.0));

    this.levels[level] = new MineLevel.Builder(level).buildLevel(this);
    Iterator<Gateway> gateways = this.levels[level-1].getGateways();
    Gateway nextGateway;
    while (gateways.hasNext()) {
      nextGateway = gateways.next();
      if (nextGateway.getDestinationArea() == null) {
        nextGateway.setDestinationArea(this.levels[level]);
        nextGateway.setDestinationGateway(this.levels[level].getEntranceGateway());
      }
    }
  }

  @Override
  public void doDayEndActions() {
    this.levels[0] = new MineLevel.Builder(0).buildLevel(this);
    this.startingGateway.setDestinationArea(this.levels[0]);
    this.startingGateway.setDestinationGateway(this.levels[0].getEntranceGateway());
    
    for (int i = 1; i < MineArea.NUM_LEVELS; ++i) {
      this.levels[i] = null;
    }
  }

  public Gateway getLevelExitGateway() {
    return this.levelExitGateway;
  }

  public Point getElevatorPosition() {
    return this.elevator.getOrigin();
  }

  public void setElevatorDestination(int level) {
    if (this.levels[level] == null) {
      this.levels[level] = new MineLevel.Builder(level).buildLevel(this);
    }
    this.elevator.setDestinationArea(this.levels[level]);
    this.elevator.setDestinationGateway(this.levels[level].getEntranceGateway());
  }

  public boolean hasElevatorFloorUnlocked(int floor) {
    return floor/5 >= this.elevatorLevelsUnlocked;
  }
}