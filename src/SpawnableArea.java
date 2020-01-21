/**
 * [SpawnableArea]
 * An interface for areas that support spawn locations.
 * This includes buildings and the mine.
 * 2020-1-19
 * @version 0.1
 * @author Joseph Wang
 */

public interface SpawnableArea {
  /**
   * [setSpawnLocation]
   * Assigns this area's stored spawn location to another point.
   * @param spawnPos The new spawn location point.
   */
  public void setSpawnLocation(Point spawnPos);

  /**
   * [getSpawnLocation]
   * Retrieves this area's stored spawn location.
   * @return Point, a clone of this building's spawn location.
   */
  public Point getSpawnLocation();
}