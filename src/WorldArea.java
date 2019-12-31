import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

/**
 * [WorldArea] 
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao, Paula Yuan
 */
public class WorldArea extends Area {
  // private TownBuilding[] houses;

  HashSet<Tile> forageableTiles = new HashSet<>(); // TODO: replace with int
  Random random = new Random();
  ArrayList<Tile> grassTiles = new ArrayList<>();
  String[] forageables = {"Bluebell", "Daffodil", "IceCream", "Javacake", 
                          "Leek", "Mushroom", "Turnip", "WinterRoot"}; 
  // TODO: add other forageables to ^^

  public WorldArea(String name,
                   int width, int height) {
    super(name, width, height);
  }

  private void spawnForageables() {
    int maxToSpawn = 6 - forageableTiles.size();
    int spawnNum = Math.min(random.nextInt(3) + 2, maxToSpawn);
    for (int i = 0; i < spawnNum; i++) {
      TileComponent forageable = IntrinsicTileComponentFactory.getComponent(forageables[random.nextInt(8)]);
      Tile spawnTile = grassTiles.get(random.nextInt(grassTiles.size()));
      if (spawnTile.getContent() == null) {
        spawnTile.setContent(forageable);
        forageableTiles.add(spawnTile);
      }
    }
  }

  @Override
  public void doDayEndActions() {
    spawnForageables();
  }

  @Override
  public void setMapAt(Tile t) {
    super.setMapAt(t);
    if (t instanceof GrassTile) {
      this.grassTiles.add(t);
    }
  }

}