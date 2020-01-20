import java.awt.image.BufferedImage;
/**
 * [HoldableStackEntity]
 * A class that stores a HoldableStack and is used to represent
 * a dropped item in the world.
 * 2019-12-20
 * @version 0.1
 * @author Kevin Qiao
 */
public class HoldableStackEntity extends Moveable implements Drawable {
  public static final double MAX_SPEED = 3;
  private HoldableStack stackObject;

  /**
   * [HoldableStackEntity]
   * A constructor for a new HoldableStackEntity.
   * @param stackObject  The HoldableStack stored.
   * @param position     The position of this entity.
   */
  public HoldableStackEntity(HoldableStack stackObject, Point position) {
    super(position, 0.125);
    this.stackObject = stackObject;
  }

  /**
   * [getMove]
   * Uses the elapsed time to figure out movement. However, this object
   * will never know how to move without the player position, so this
   * will always return a new Vector2D of 0, 0.
   * @param elapsedNanoTime  The elapsed amount of time in the world.
   * @return                 Vector2D, equal to (0, 0).
   */
  @Override
  public Vector2D getMove(long elapsedNanoTime) {
    // does not know how to move without player pos
    return new Vector2D(0, 0);
  }

  /**
   * [getMove]
   * Using the player's position and elapsed time, calculates how the
   * items in this HoldableStackEntity will move. Uses velocity.
   * @param elapsedNanoTime  The elapsed amount of time in the world.
   * @param playerPos        The player position.
   * @return                 Vector2D, with this velocity scaled to the 
   *                         elapsed time.
   */
  public Vector2D getMove(long elapsedNanoTime, Point playerPos) {
    this.setVelocity(
        playerPos.x-this.getPos().x+(Math.random()-0.5),
        playerPos.y-this.getPos().y+(Math.random()-0.5),
        Math.min((double)Player.getItemAttractionDistance()/this.getPos().distanceTo(playerPos),
                 HoldableStackEntity.MAX_SPEED)
    );
    return this.getVelocity().scale(elapsedNanoTime/1_000_000_000.0);
  }

  /**
   * [getStack]
   * Gets the HoldableStack in this entity.
   * @return HoldableStack, the HoldableStack in this entity.
   */
  public HoldableStack getStack() {
    return this.stackObject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.stackObject.getContainedHoldable().getSmallImage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getXOffset() {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getYOffset() {
    return 0;
  }
}