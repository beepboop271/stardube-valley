
/**
 * [Player]
 * 2019-12-17
 * @version 0.1
 * @author Kevin Qiao
 */
public class Player extends Moveable {
  private static final double size = 0.35;
  private static final double maxSpeed = 4;
  private Vector2D velocity;
  private boolean inMenu = false;
  private int inventorySize = 12;
  private HoldableStack[] inventory;

  public Player(Point position) {
    super(position, Player.size);
    this.velocity = new Vector2D(0, 0);
    this.inventory = new HoldableStack[this.inventorySize];
  }

  public void setHorizontalSpeed(int dx) {
    this.velocity.setPos(dx, this.velocity.getY());
  }

  public void setVerticalSpeed(int dy) {
    this.velocity.setPos(this.velocity.getX(), dy);
  }

  public void makeMove(long elapsedNanoTime) {
    double elapsedSeconds = elapsedNanoTime/1_000_000_000.0;
    Vector2D positionChange = (Vector2D)this.velocity.clone();
    positionChange.setLength(Player.maxSpeed*elapsedSeconds);
    this.translatePos(positionChange.getX(), positionChange.getY());
  }

  public boolean isInMenu() {
    return this.inMenu;
  }

  public void toggleInMenu() {
    this.inMenu = !this.inMenu;
  }

  public void setInMenu(boolean inMenu) {
    this.inMenu = inMenu;
  }

  public static double getSize() {
    return Player.size;
  }
}