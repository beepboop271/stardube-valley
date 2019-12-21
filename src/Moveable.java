import java.util.LinkedHashSet;

/**
 * [Moveable]
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */
public abstract class Moveable {
  private Point position;
  private double size;

  public Moveable(Point position, double size) {
    this.position = position;
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

  public void setPos(Point position) {
    this.position = ((Point)position.clone());
  }

  abstract void makeMove(long elapsedNanoTime);
}