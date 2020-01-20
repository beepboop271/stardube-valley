import java.awt.image.BufferedImage;

public class ImageSequenceAnimation extends Animation {
  private BufferedImage[] images;
  private int currentImageIdx;
  private static int nanoTimeFrameDuration;

  public ImageSequenceAnimation() {
    super();
    this.currentImageIdx = 0;
  }

  public BufferedImage nextImage() {
    if (this.getElapsedNanoTime() >= ImageSequenceAnimation.nanoTimeFrameDuration) {
      ++currentImageIdx;
      this.updateLastUpdateNanoTime();
    }
    return this.images[currentImageIdx];
  }
}