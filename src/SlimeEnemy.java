public class SlimeEnemy extends Enemy {
  private static final double TYPICAL_SPEED = 1;

  public SlimeEnemy(Point position) {
    super(position, 0.3, 1, "slime", 3);
  }

  @Override
  Vector2D getMove(long elapsedNanoTime) {
    if (Math.random() < 0.05) {
      this.setVelocity(Math.random()-0.5, Math.random()-0.5, SlimeEnemy.TYPICAL_SPEED);
      this.updateOrientation();
    }
    return this.getVelocity().scale(elapsedNanoTime/1_000_000_000.0);
  }

  public Vector2D getMove(long elapsedNanoTime, Point playerPos) {
    this.setVelocity(playerPos.x-this.getPos().x+(Math.random()-0.5),
                     playerPos.y-this.getPos().y+(Math.random()-0.5),
                     SlimeEnemy.TYPICAL_SPEED);
    this.updateOrientation();
    return this.getVelocity().scale(elapsedNanoTime/1_000_000_000.0);
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