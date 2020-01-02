public class MineArea extends Area {
  private static final int NUM_LEVELS = 120;

  private MineLevel[] levels;
  
  public MineArea(String name,
                  int width, int height) {
    super(name, width, height);
  }

  @Override
  public void doDayEndActions() {
    for (int i = 0; i < MineArea.NUM_LEVELS; ++i) {
      this.levels[i] = null;
    }
  }
}