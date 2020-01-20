import java.awt.image.BufferedImage;

public class HoldableStackEntity extends Moveable implements Drawable {
  public static final double MAX_SPEED = 3;
  private HoldableStack stackObject;

  public HoldableStackEntity(HoldableStack stackObject, Point position) {
    super(position, 0.125);
    this.stackObject = stackObject;
  }

  @Override
  public Vector2D getMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.scale(elapsedSeconds);
    // this.translatePos(positionChange);
    return positionChange;
  }

  public HoldableStack getStack() {
    return this.stackObject;
  }

  @Override
  public BufferedImage getImage() {
    return this.stackObject.getContainedHoldable().getSmallImage();
  }

  @Override
  public double getXOffset() {
    return 0;
  }

  @Override
  public double getYOffset() {
    return 0;
  }
}