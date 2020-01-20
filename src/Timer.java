public class Timer { //TODO: JAVADOCS
  private long startTime;
  private long duration;

  public Timer(long duration) {
    // duration in seconds
    this.duration = duration*(1_000_000_000);
    this.startTime = System.nanoTime();
  }

  public boolean isDone() {
    return System.nanoTime()-this.startTime >= this.duration;
  }
}