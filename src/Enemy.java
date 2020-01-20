public abstract class Enemy extends LoopAnimatedMoveable {
  private final int height;
  private int health;
  private long lastOrientationUpdateNanoTime;
  private static final long ORIENTATION_UPDATE_PAUSE = (long)(0.5*1_000_000_000);

  public Enemy(Point position, double size, int height,
               String name, int framesPerSecond) {
    super(position, size, "enemies/"+name, framesPerSecond);
    this.setOrientation(World.SOUTH);
    this.lastOrientationUpdateNanoTime = System.nanoTime();
    this.height = height;
  }

  public void updateOrientation() {
    if (System.nanoTime()-this.lastOrientationUpdateNanoTime > Enemy.ORIENTATION_UPDATE_PAUSE) {
      this.setOrientation(this.getVelocity().asMoveInteger());
      this.lastOrientationUpdateNanoTime = System.nanoTime();
    }
  }

  public int getHeight() {
    return this.height;
  }

  public int getHealth() {
    return this.health;
  }

  public abstract Vector2D getMove(long elapsedNanoTime, Point playerPos);
}