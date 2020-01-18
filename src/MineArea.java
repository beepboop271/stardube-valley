public class MineArea extends Area {
  private static final int NUM_LEVELS = 120;

  private MineLevel[] levels;
  private Point startingLadder;
  private Point elevator;
  
  public MineArea(String name,
                  int width, int height) {
    super(name, width, height);
    this.levels = new MineLevel[MineArea.NUM_LEVELS];
    this.levels[0] = new MineLevel.Builder(0, 2).buildLevel();
    this.levels[1] = new MineLevel.Builder(1, 2).buildLevel();
  }

  @Override
  public void doDayEndActions() {
    for (int i = 1; i < MineArea.NUM_LEVELS; ++i) {
      this.levels[i] = null;
    }
  }
}