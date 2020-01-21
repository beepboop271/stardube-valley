import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    WaterTile.setWaterTileImage();
    GrassTile.setGrassTileImage();
    GroundTile.setGroundTileImages();
    DecorationTile.setTileImages();
    MineGatewayTile.setLadderImages();

    WaterTile.setFishableTrash();
    PondTile.setFishableFish();
    RiverTile.setFishableFish();
    LakeTile.setFishableFish();
    OceanTile.setFishableFish();

    HoldableFactory.initializeItems();
    IntrinsicTileComponentFactory.initializeComponents();
    
    MineLevel.loadComponents();

    // menu display(start game, lore, credits)
    MenuSystemFrame menuDisplay = new MenuSystemFrame();
    MenuSystemEventListener menuSystemListener = ((MenuSystemPanel)menuDisplay.getContentPane()).getListener();
    while (!(menuSystemListener.shouldStartGame())) {
      menuDisplay.refresh();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
    }

    // exits menu system and game starts
    World stardube = new World();
    StardubeFrame gameDisplay = new StardubeFrame(stardube);
    StardubeEventListener gameListener = ((WorldPanel)gameDisplay.getContentPane()).getListener();
    menuDisplay.dispose(); // dispose after the new display has been initialized

    while (true) {
      gameListener.update();
      stardube.update();
      gameDisplay.refresh();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
    }
  }
}