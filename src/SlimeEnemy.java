public class SlimeEnemy extends Enemy {

  public SlimeEnemy(Point position) {
    super(position, 0.3, 1, "slime");
  }

  @Override
  Vector2D getMove(long elapsedNanoTime) {
    return null;
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