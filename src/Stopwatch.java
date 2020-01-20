public class Stopwatch { //TODO: JAVADOCS
  private long startNanoTime;

  public Stopwatch() {
    this.startNanoTime = System.nanoTime();
  }

  public long getNanoTimeElapsed() {
    return System.nanoTime()-this.startNanoTime;
  }
}