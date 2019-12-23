public class HoldableStackEntity extends Moveable {
  private HoldableStack stackObject;

  public HoldableStackEntity(HoldableStack stackObject, Point position) {
    super(position, 0.1);
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
}