public abstract class Enemy extends LoopAnimatedMoveable {
  private final int height;
  private int health;

  public Enemy(Point position, double size, int height,
               String name) {
    super(position, size, "enemies/"+name);
    this.height = height;
  }

  public int getHeight() {
    return this.height;
  }

  public int getHealth() {
    return this.health;
  }
}