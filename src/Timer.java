public class Timer {
  private long startTime;
  private double duration;

  public Timer(double duration) {
    // duration in seconds
    this.duration = duration*(1_000_000_000);
    this.startTime = System.nanoTime();
  }

  public boolean isDone() {
    return System.nanoTime()-this.startTime >= this.duration;
  }
}