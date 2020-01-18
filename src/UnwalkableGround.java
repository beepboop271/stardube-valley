/**
 * [UnwalkableGround]
 * A ground tile that preserves the image but cannot be walked on.
 * 2020-01-18
 * @version 0.1
 * @author Joseph Wang
 */

public class UnwalkableGround extends GroundTile implements NotWalkable {
  /**
   * [UnwalkableGround]
   * Constructor for a new UnwalkableGround.
   * @param x The x pos of this ground tile.
   * @param y The y pos of this ground tile.
   */
  public UnwalkableGround(int x, int y) {
    super(x, y);
  }
}