/**
 * [FishingGameBar]
 * 2019-12-24
 * @version 0.1
 * @author Candice Zhang
 */

class FishingGameBar {
  private double y;
  private final int height;
  private double velocity;

  public FishingGameBar(int y, int height) {
    this.y = y;
    this.height = height;
    this.velocity = 0;
  }

  public FishingGameBar(int y, int height, double velocity) {
    this.y = y;
    this.height = height;
    this.velocity = velocity;
  }

  public boolean isInside(FishingGameBar otherBar) {
    return ((this.y>=otherBar.getY()) && ((this.y+this.height)<=(otherBar.getY()+otherBar.getHeight())));
  }

  public double getY() {
    return this.y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public int getHeight() {
    return this.height;
  }

  public double getVelocity() {
    return this.velocity;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  public void negateVelocity() {
    this.velocity *= -1;
  }
}