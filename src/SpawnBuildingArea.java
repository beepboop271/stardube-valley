/**
 * [SpawnBuildingArea]
 * A building that can support player spawning.
 * 2020-1-19
 * @version 0.1
 * @author Joseph Wang
 */
public class SpawnBuildingArea extends BuildingArea implements SpawnableArea{
  private Point spawnLocation;

  /**
   * [SpawnBuildingArea]
   * Constructor for a new SpawnBuildingArea.
   * @param name    The name of this building.
   * @param width   The tile width of this building.
   * @param height  The tile height of this building.
   */
  public SpawnBuildingArea(String name, int width, int height) {
    super(name, width, height);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSpawnLocation(Point spawnPos) {
    this.spawnLocation = spawnPos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Point getSpawnLocation() {
    return (Point)this.spawnLocation.clone();
  }
}