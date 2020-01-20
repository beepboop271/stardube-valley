import java.awt.image.BufferedImage;

/**
 * [VerticalImageTranslationAnimation] 
 * an animation for vertical image translation
 * 2020-01-17
 * @version 0.1
 * @author Kevin Qiao
 */
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