import java.util.LinkedHashSet;

/**
 * [Moveable]
 * All the objects that are able to move around in the world.
 * 2019-12-19
 * @version 0.2
 * @author Kevin Qiao, Candice Zhang, Paula Yuan
 */
public abstract class Moveable implements Drawable {
  private Point position;
  private Vector2D velocity;
  private double size;

  /**
   * [Moveable]
   * Constructor for a new Moveable.
   * @param position  The position of this moveable.
   * @param size      How large this moveable is.
   */
  public Moveable(Point position, double size) {
    this.position = position;
    this.velocity = new Vector2D(0, 0);
    this.size = size;
  }

  //TODO: finish this
  /**
   * [getIntersectingTiles] 
   * @param offset 
   * @return LinkedHashSet<Point>, all the intersecting tiles.
   */
  public LinkedHashSet<Point> getIntersectingTiles(Vector2D offset) {
    LinkedHashSet<Point> intersections = new LinkedHashSet<Point>();
    // size will always be < 2 so this is enough
    // System.out.println(this.position);
    Point movedPosition = this.position.translateNew(offset.getX(), offset.getY());
    intersections.add(movedPosition.translateNew(-this.size, -this.size).round());
    intersections.add(movedPosition.translateNew(this.size, -this.size).round());
    intersections.add(movedPosition.translateNew(this.size, this.size).round());
    intersections.add(movedPosition.translateNew(-this.size, this.size).round());
    return intersections;
  }

  /**
   * [getPos]
   * Retrieves the position of this moveable.
   * @return Point, a clone of the position.
   */
  public Point getPos() {
    return ((Point)this.position.clone());
  }

  /**
   * [translatePos]
   * Translates the position using the specified x and y.
   * @param dx  The change in x to translate.
   * @param dy  The change in y to translate.
   */
  public void translatePos(double dx, double dy) {
    this.position.translate(dx, dy);
  }

  /**
   * [translatePos]
   * Translate the position using the specified Vector2D.
   * @param positionChange  The Vector2D with both x and y changes.
   */
  public void translatePos(Vector2D positionChange) {
    this.position.translate(positionChange.getX(), positionChange.getY());
  }

  /**
   * [setPos]
   * Sets this moveable's position to a cloned specified position.
   * @param position, the new position for this moveable.
   */
  public void setPos(Point position) {
    this.position = ((Point)position.clone());
  }

  /**
   * [getVelocity]
   * Retrieves the velocity of this moveable.
   * @return Vector2D, with this moveable's clone velocity.d
   */
  public Vector2D getVelocity() {
    return (Vector2D)this.velocity.clone();
  }

  /**
   * [setVelocity]
   * Sets this position and length to specified x, y and length
   * @param dx      The change in x.
   * @param dy      The change in y.
   * @param length  The new length to set.
   */
  public void setVelocity(double dx, double dy, double length) {
    this.velocity.setPos(dx, dy);
    this.velocity.setLength(length);
  }

  /**
   * [getHorizontalSpeed]
   * Gets the x value from this moveable's velocity Vector2D.
   * @return int, the horizontal speed of this moveable.
   */
  public int getHorizontalSpeed() {
    return (int)this.velocity.getX();
  }

  /**
   * [setHorizontalSpeed]
   * Sets the x value from this moveable's velocity Vector2D to
   * specified value.
   * @param dx  The change to the x value.
   */
  public void setHorizontalSpeed(double dx) {
    this.velocity.setPos(dx, this.velocity.getY());
  }

  /**
   * [getVerticalSpeed]
   * Gets the y value from this moveable's velocity Vector2D.
   * @return int, the vertical speed of this moveable.
   */
  public int getVerticalSpeed() {
    return (int)this.velocity.getY();
  }

  /**
   * [setVericalSpeed]
   * Sets the y value from this moveable's velocity Vector2D to
   * specified value.
   * @param dx  The change to the y value.
   */
  public void setVerticalSpeed(double dy) {
    this.velocity.setPos(this.velocity.getX(), dy);
  }
  
  /**
   * [getSize]
   * Retrieves the size of this moveable.
   * @return double, this moveable's size.
   */
  public double getSize() {
    return this.size;
  }

  //TODO: finish this
  /**
   * [getMove]
   * @param elapsedNanoTime
   * @return Vector2D
   */
  abstract Vector2D getMove(long elapsedNanoTime);
}