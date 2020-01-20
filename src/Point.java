/**
 * [Point]
 * A class for keeping track of locations in the world.
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class Point implements Cloneable {
  double x;
  double y;

  /**
   * [Point]
   * Constructor for a new Point.
   * @param x  The x position of this point.
   * @param y  The y position of this point.
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * [Point]
   * Constructor for a new Point.
   * @param p  A string array with the x and y positions of this point.
   */
  public Point(String[] p) {
    this.x = Double.parseDouble(p[0]);
    this.y = Double.parseDouble(p[1]);
  }

  /**
   * [Point]
   * Constructor for a new Point.
   * @param p  A Point with the x and y positions of this point.
   */
  public Point(Point p) {
    this(p.x, p.y);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format("P(%.2f, %.2f)", this.x, this.y);
  }

  /**
   * [translate]
   * Shifts this point to another location using the specified dx and dy.
   * @param dx  The change in x.
   * @param dy  The change in y.
   * @return Point, this Point object.
   */
  public Point translate(double dx, double dy) {
    this.x += dx;
    this.y += dy;
    return this;
  }

  /**
   * [translate]
   * Shifts this point to another location using the specified point.
   * @param p  The new point with the dx and dy.
   * @return Point, this Point object.
   */
  public Point translate(Point p) {
    return this.translate(p.x, p.y);
  }

  /**
   * [translateNew]
   * Creates a new point which takes this Point's x and y and adds the
   * specified dx and dy.
   * @param dx  The change in x.
   * @param dy  The change in y.
   * @return    Point, a new Point with the new location.
   */
  public Point translateNew(double dx, double dy) {
    return new Point(this).translate(dx, dy);
  }

  /**
   * [distanceTo]
   * Retrieves the distance to the other point.
   * @param other  The point to calculate distance with.
   * @return       double, the distance to the other point.
   */
  public double distanceTo(Point other) {
    return Math.sqrt(Math.pow(other.x-this.x, 2) + Math.pow(other.y-this.y, 2));
  }

  /**
   * [round]
   * Calls Math.round on both this x and y, effectively rounding it.
   * @return Point, this Point object.s
   */
  public Point round() {
    this.x = Math.round(this.x);
    this.y = Math.round(this.y);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    // auto generated
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(this.x);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.y);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    // auto generated
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (this.getClass() != obj.getClass()) {
      return false;
    }
    Point other = (Point)obj;
    if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
      return false;
    } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
      return false;
    }
    return true;
  }
}