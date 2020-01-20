/**
 * [Animatable]
 * A public interface that deals with an object's animations.
 * 2020-01-18
 * @version 0.1
 * @author Kevin Qiao
 */
public interface Animatable {
  /**
   * [isAnimating]
   * Checks if the object is animating.
   * @return boolean, true if the object is animating, false otherwise.
   */
  public boolean isAnimating();
  
  /**
   * [setAnimating]
   * Sets whether the object is animating.
   * @param isAnimating boolean, true if the object is animating, false otherwise.
   */
  public void setAnimating(boolean isAnimating);
}