/**
 * [Point]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public class Point implements Cloneable {
  double x;
  double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Point(Point p) {
    this(p.x, p.y);
  }
  
  @Override
  public String toString() {
    return String.format("P(%.2f, %.2f)", this.x, this.y);
  }

  public Point translate(double dx, double dy) {
    this.x += dx;
    this.y += dy;
    return this;
  }

  public Point translateNew(double dx, double dy) {
    return new Point(this).translate(dx, dy);
  }

  public Point round() {
    this.x = Math.round(this.x);
    this.y = Math.round(this.y);
    return this;
  }

  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }

  @Override
  public int hashCode() {
    // auto generated
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

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