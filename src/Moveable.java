import java.util.LinkedHashSet;

/**
 * [Moveable]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class Moveable {
  private Point position;
  private Vector2D velocity;
  private double size;

  // should probably have made new abstract subclass
  // for moveables that follow certain velocity bc
  // not all moveables will be like this but thats
  // for another time

  public Moveable(Point position, double size) {
    this.position = position;
    this.velocity = new Vector2D(0, 0);
    this.size = size;
  }

  public LinkedHashSet<Point> getIntersectingTiles() {
    LinkedHashSet<Point> intersections = new LinkedHashSet<Point>();
    // size will always be < 2 so this is enough
    intersections.add(this.position.translateNew(this.size, -this.size).round());
    intersections.add(this.position.translateNew(this.size, this.size).round());
    intersections.add(this.position.translateNew(-this.size, this.size).round());
    intersections.add(this.position.translateNew(-this.size, -this.size).round());
    return intersections;
  }

  public Point getPos() {
    return ((Point)this.position.clone());
  }

  public void translatePos(double dx, double dy) {
    this.position.translate(dx, dy);
  }

  public void translatePos(Vector2D positionChange) {
    this.position.translate(positionChange.getX(), positionChange.getY());
  }

  public void setPos(Point position) {
    this.position = ((Point)position.clone());
  }

  public Vector2D getVelocity() {
    return (Vector2D)this.velocity.clone();
  }

  public void setVelocity(double dx, double dy, double length) {
    this.velocity.setPos(dx, dy);
    this.velocity.setLength(length);
  }

  public int getHorizontalSpeed() {
    return (int)this.velocity.getX();
  }

  public void setHorizontalSpeed(double dx) {
    this.velocity.setPos(dx, this.velocity.getY());
  }

  public int getVerticalSpeed() {
    return (int)this.velocity.getY();
  }

  public void setVerticalSpeed(double dy) {
    this.velocity.setPos(this.velocity.getX(), dy);
  }

  abstract void makeMove(long elapsedNanoTime);
}