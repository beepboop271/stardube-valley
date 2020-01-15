import java.awt.Graphics;

/**
 * [TimedGraphic]
 * 2020-01-15
 * @version 0.1
 * @author Candice Zhang
 */

abstract class TimedGraphic {
  private Timer timer;

  TimedGraphic(double duration) {
    this.timer = new Timer(duration);
  }

  public boolean isDone() {
     return this.timer.isDone();
  }

  abstract void draw(Graphics g, int x, int y);
}