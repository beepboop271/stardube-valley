import java.io.IOException;

/**
 * HarvestableComponent 2019-12-21
 * 
 * @version 0.2
 * @author Kevin Qiao, Paula Yuan
 */
public class ExtrinsicHarvestableTileComponent extends ExtrinsicTileComponent {
  private int hardnessLeft;

  public ExtrinsicHarvestableTileComponent(String name) throws IOException {
    super(name);
    this.hardnessLeft = ((IntrinsicHarvestableTileComponent)this.getIntrinsicSelf()).getHardness();
  }

  public boolean damageComponent(int damage) {
    this.hardnessLeft -= damage;
    return (this.hardnessLeft <= 0);
  }

}