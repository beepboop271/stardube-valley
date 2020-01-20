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
    // does not know how to move without player pos
    return new Vector2D(0, 0);
  }

  public Vector2D getMove(long elapsedNanoTime, Point playerPos) {
    this.setVelocity(
        playerPos.x-this.getPos().x+(Math.random()-0.5),
        playerPos.y-this.getPos().y+(Math.random()-0.5),
        Math.min((double)Player.getItemAttractionDistance()/this.getPos().distanceTo(playerPos),
                 HoldableStackEntity.MAX_SPEED)
    );
    return this.getVelocity().scale(elapsedNanoTime/1_000_000_000.0);
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