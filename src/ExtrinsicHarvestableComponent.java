
/**
 * HarvestableComponent 2019-12-21
 * 
 * @version 0.2
 * @author Kevin Qiao, Paula Yuan
 */
public class ExtrinsicHarvestableComponent extends ExtrinsicTileComponent implements NotWalkable {
  private int hardnessLeft;

  public ExtrinsicHarvestableComponent(String name) {
    super(name);
    this.hardnessLeft = ((IntrinsicHarvestableComponent)this.getIntrinsicSelf()).getHardness();
  }

  public boolean damageComponent(int damage) {
    this.hardnessLeft -= damage;
    return (this.hardnessLeft <= 0);
  }

  public void setHardnessLeft(int hardnessLeft) {
    this.hardnessLeft = hardnessLeft;
  }
}