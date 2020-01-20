import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    GroundTile.setGroundTileImages();
    GrassTile.setGrassTileImage();
    DecorationTile.initializeTileImages();
    WaterTile.setWaterTileImage();
    WaterTile.setFishableTrash();
    PondTile.setFishableFish();
    RiverTile.setFishableFish();
    LakeTile.setFishableFish();
    OceanTile.setFishableFish();
    MineGatewayTile.setLadderImage();

    HoldableFactory.initializeItems();
    IntrinsicTileComponentFactory.initializeComponents();
    
    MineLevel.loadComponents();

    World stardube = new World();
    StardubeFrame display = new StardubeFrame(stardube);
    StardubeEventListener listener = ((WorldPanel)display.getContentPane()).getListener();
    System.out.println("Welcome to Stardube Valley!");
    while (true) {
      listener.update();
      stardube.update();
      display.refresh();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
    }
  }
}