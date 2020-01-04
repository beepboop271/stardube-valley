import java.awt.image.BufferedImage;

public class HoldableStackEntity extends Moveable implements Drawable {
  private HoldableStack stackObject;

  public HoldableStackEntity(HoldableStack stackObject, Point position) {
    super(position, 0.125);
    this.stackObject = stackObject;
  }

  @Override
  public void makeMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = this.getVelocity();
    positionChange.scale(elapsedSeconds);
    this.translatePos(positionChange);
  }

  public HoldableStack getStack() {
    return this.stackObject;
  }

  @Override
  public BufferedImage getImage() {
    return this.stackObject.getContainedHoldable().getSmallImage();
  }

  @Override
  public int getXOffset() {
    return 0;
  }

  @Override
  public int getYOffset() {
    return 0;
  }
}