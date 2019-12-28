/**
 * [FishingGameBar]
 * 2019-12-24
 * @version 0.1
 * @author Candice Zhang
 */

class FishingGameBar {
  private int y;
  private int height;

  FishingGameBar(int y, int height) {
    this.y = y;
    this.height = height;
  }

  public boolean isInside(FishingGameBar otherBar) {
    return ((this.y>=otherBar.getY()) && ((this.y+this.height)<=(otherBar.getY()+otherBar.getHeight())));
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getHeight() {
    return this.height;
  }

}