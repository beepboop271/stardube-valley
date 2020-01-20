/**
 * [FishingGameBar]
 * A compoenent of a fishing game that stores essential data, including y position, height and velocity.
 * 2019-12-24
 * @version 0.2
 * @author Candice Zhang
 */

class FishingGameBar {
  private double y;
  private final int height;
  private double velocity;

  /**
   * [FishingGameBar]
   * Constructor for a new FishingGameBar that has a initial velocity of 0.
   * @param y       int, the y position of the FishingGameBar.
   * @param height  int, the height of the FishingGameBar.
   */
  public FishingGameBar(int y, int height) {
    this.y = y;
    this.height = height;
    this.velocity = 0;
  }
  
  /**
   * [FishingGameBar]
   * Constructor for a new FishingGameBar that does not have a initial velocity of 0.
   * @param y         int, the y position of the FishingGameBar.
   * @param height    int, the height of the FishingGameBar.
   * @param velocity  double, the velocity of the FishingGameBar.
   */
  public FishingGameBar(int y, int height, double velocity) {
    this.y = y;
    this.height = height;
    this.velocity = velocity;
  }

  /**
   * [isInside]
   * Checks if this FishingGameBar is inside another FishingGameBar.
   * @return boolean, true if this is inside another FishingGameBar, false otherwise.
   */
  public boolean isInside(FishingGameBar otherBar) {
    return ((this.y>=otherBar.getY()) && ((this.y+this.height)<=(otherBar.getY()+otherBar.getHeight())));
  }

  /**
   * [getY]
   * Retrieves the y position of this FishingGameBar.
   * @return double, the y position of this FishingGameBar.
   */
  public double getY() {
    return this.y;
  }

  /**
   * [setY]
   * Sets the y position of this FishingGameBar.
   * @param y double, the y position of this FishingGameBar.
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * [getHeight]
   * Retrieves the height of this FishingGameBar.
   * @return int, the height of this FishingGameBar.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * [getVelocity]
   * Retrieves the velocity of this FishingGameBar.
   * @return double, the velocity of this FishingGameBar.
   */
  public double getVelocity() {
    return this.velocity;
  }

  /**
   * [setVelocity]
   * Sets the velocity of this FishingGameBar.
   * @param velocity double, the velocity of this FishingGameBar.
   */
  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  /**
   * [negateVelocity]
   * Negates the velocity of this FishingGameBar.
   */
  public void negateVelocity() {
    this.velocity *= -1;
  }
}