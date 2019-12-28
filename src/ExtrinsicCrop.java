import java.awt.image.BufferedImage;

public class ExtrinsicCrop implements Placeable {
  private IntrinsicCrop crop;
  private int stage, maxGrowthStage;

  public EntrinsicCrop(IntrinsicCrop crop) {

  }

  @Override
  public BufferedImage getImage() {
    return this.crop.getImages().get(this.crop.getStageToDisplay(stage));
  }
}