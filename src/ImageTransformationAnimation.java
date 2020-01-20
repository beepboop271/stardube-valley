import java.awt.image.BufferedImage;

public abstract class ImageTransformationAnimation extends Animation {
  private BufferedImage originalImage;
  private final double acceleration;
  private double velocity;

  public ImageTransformationAnimation(BufferedImage originalImage,
                                      double acceleration, double velocity) {
    this.originalImage = originalImage;
    this.acceleration = acceleration;
    this.velocity = velocity;
  }

  public double updateVelocity(long elapsedNanoTime) {
    this.velocity += this.acceleration*((double)elapsedNanoTime/1_000_000_000);
    return this.velocity;
  }
}