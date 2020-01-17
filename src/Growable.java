/**
 * [Growable]
 * An interface for any object that is able to grow (ie. increase stages to reach a 
 * max stage or full growth).
 * 2020-1-1
 * @version 0.1
 * @author Joseph Wang
 */

public interface Growable {
  /**
   * [grow]
   * Performs this growable's grow action, usually by increasing its stage.
   */
  public void grow();
}