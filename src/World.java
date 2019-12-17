import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * [World]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public class World {
  private LinkedHashSet<Area> locations;

  public World(String[] locations) {
    this.locations = new LinkedHashSet<Area>();
    this.loadAreas(locations);
  }

  public void loadAreas(String[] locations) {
    BufferedReader reader;
    Area newArea;
    String nextLine;
    Tile newTile;
    for (int i = 0; i < locations.length; ++i) {
      try {
        reader = new BufferedReader(new FileReader(locations[i]));
        newArea = Area.constructArea(reader.readLine(),
                                     reader.readLine(),
                                     reader.readLine().charAt(0),
                                     Integer.parseInt(reader.readLine()),
                                     Integer.parseInt(reader.readLine()));
        this.locations.add(newArea);
        for (int y = 0; y < newArea.getHeight(); ++y) {
          nextLine = reader.readLine();
          for (int x = 0; x < newArea.getWidth(); ++x) {
            switch (nextLine.charAt(x)) {
              case '.':
                newTile = new GroundTile(x, y);
                break;
              case ' ':
                newTile = null;
                break;
              default:
                newTile = 
            }
            newArea.setMapAt(newTile);
          }
        }
        reader.close();
      } catch (FileNotFoundException e) {
        System.out.println("Could not open file "+locations[i]);
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}