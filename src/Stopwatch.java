/**
 * [Stopwatch]
 * A class that keeps track of elapsed time since creation.
 * 2020-01-08
 * @version 0.2
 * @author Candice Zhang, Kevin Qiao
 */

public class Stopwatch {
  private long startNanoTime;
  
  /**
   * [Stopwatch]
   * Constructor for a new Stopwatch. Starts the stopwatch
   * on creation.
   */
  public Stopwatch() {
    this.startNanoTime = System.nanoTime();
  }

  /**
   * [getNanoTimeElapsed]
   * Retrieves the time elapsed since creation of this stopwatch.
   * @return long, the time elapsed.
   */
  public long getNanoTimeElapsed() {
    return System.nanoTime()-this.startNanoTime;
  }
}