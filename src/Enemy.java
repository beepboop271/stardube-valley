public abstract class Enemy extends Moveable {
  private final int height;
  private int health;

  public Enemy(Point position, double size, int height) {
    super(position, size);
    this.height = height;
  }

  public int getHeight() {
    return this.height;
  }
}