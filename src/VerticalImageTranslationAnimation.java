import java.awt.image.BufferedImage;

public class VerticalImageTranslationAnimation extends ImageTransformationAnimation {
  private Point offset;

  public VerticalImageTranslationAnimation(BufferedImage originalImage,
                                   double acceleration, double velocity) {
    super(originalImage, acceleration, velocity);
  }

  public BufferedImage nextImage() {
    long time = this.getElapsedNanoTime();
    
    this.offset.y += this.updateVelocity(time)*((double)time/1_000_000_000);
        
    this.updateLastUpdateNanoTime();
    return null;
  }
}