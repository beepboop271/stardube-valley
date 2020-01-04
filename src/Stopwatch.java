public class Stopwatch {
  private long startTime;

  public Stopwatch() {
    this.startTime = System.nanoTime();
  }

  public long getNanoTimeElapsed() {
    return System.nanoTime()-this.startTime;
  }
}