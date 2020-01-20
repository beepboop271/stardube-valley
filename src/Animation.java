import java.awt.image.BufferedImage;

public abstract class Animation implements Cloneable { //TODO: JAVADOCS
  private boolean doneAnimating;
  private long lastUpdateNanoTime;

  public Animation() {
    this.doneAnimating = false;
    this.lastUpdateNanoTime = System.nanoTime();
  }
  
  /** 
   * [clone]
   * Creates a shallow copy of the Animation.
   * @return Object, the copied Animation, or null if the copy failed.
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e){
      return null;
    }
  }

  public boolean isDoneAnimating() {
    return this.doneAnimating;
  }

  public long getElapsedNanoTime() {
    return System.nanoTime()-this.lastUpdateNanoTime;
  }

  public void updateLastUpdateNanoTime() {
    this.lastUpdateNanoTime = System.nanoTime();
  }

  public abstract BufferedImage nextImage();
}