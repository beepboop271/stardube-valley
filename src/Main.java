public class Main {
  public static void main(String[] args) {
    // sketchyy
    GroundTile.setGroundTileImages();
    GrassTile.setGrassTileImage();
    WaterTile.setWaterTileImage();
    WaterTile.setFishableTrash();
    PondTile.setFishableFish();
    RiverTile.setFishableFish();
    LakeTile.setFishableFish();
    OceanTile.setFishableFish();
    HoldableFactory.initializeItems();
    IntrinsicTileComponentFactory.initializeComponents();
    MineLevel.loadComponents();

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