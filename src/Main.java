public class Main {
  public static void main(String[] args) {
    // sketchy
    GroundTile.setGroundTileImage();
    TileComponentFactory.initializeComponents();

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