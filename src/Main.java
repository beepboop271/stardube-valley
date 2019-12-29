public class Main {
  public static void main(String[] args) {
    // sketchy
    GroundTile.setGroundTileImage();
    GrassTile.setGrassTileImage();
    WaterTile.setWaterTileImage();
    WaterTile.setFishableTrash();
    PondTile.setFishableFish();
    RiverTile.setFishableFish();
    LakeTile.setFishableFish();
    OceanTile.setFishableFish();
    HoldableFactory.initializeItems();
    IntrinsicTileComponentFactory.initializeComponents();

    World stardube = new World();
    StardubeFrame display = new StardubeFrame(stardube);
    while (true) {
      stardube.update();
      display.refresh();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
    }
  }
}