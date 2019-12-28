public class MineArea extends Area {
  private static final int NUM_LEVELS = 120;

  private MineLevel[] levels;
  
  public MineArea(String name,
                  int width, int height) {
    super(name, width, height);
  }

  @Override
  public void doDayEndActions() {
    this.levels = new MineLevel[MineArea.NUM_LEVELS];
  }
}