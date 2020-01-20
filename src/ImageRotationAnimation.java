import java.awt.image.BufferedImage;

public class ImageRotationAnimation extends ImageTransformationAnimation {
  private double angle;

  public ImageRotationAnimation(BufferedImage originalImage,
                                double acceleration, double velocity) {
    super(originalImage, acceleration, velocity);
  }

  @Override
  public BufferedImage nextImage() {
    return null;
  }
}