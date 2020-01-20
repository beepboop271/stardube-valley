
/**
 * [ExtrinsicHarvestableComponent]
 * A harvestable component that is able to store seperate
 * data from other instances, but still relies on
 * IntrinsicHarvestableComponent for some share data. 
 * 2019-12-21
 * @version 0.3
 * @author Kevin Qiao, Paula Yuan
 */
public class ExtrinsicHarvestableComponent extends ExtrinsicTileComponent implements NotWalkable {
  private int hardnessLeft;

  /**
   * [ExtrinsicHarvestableComponent]
   * Constructor for a new ExtrinsicHarvestableComponent.
   * @author      Kevin Qiao, Paula Yuan
   * @param name  The name of this harvestable component.
   */
  public ExtrinsicHarvestableComponent(String name) {
    super(name);
    this.hardnessLeft = ((IntrinsicHarvestableComponent)this.getIntrinsicSelf()).getHardness();
  }

  /**
   * [damageComponent]
   * Inflicts a certain amount of damage to this component and
   * checks to see if this component has been destroyed (ie has
   * 0 or less health).
   * @author unknown
   * @param damage  The amount of damage to deal to this component
   * @return        boolean, true if this component has less than 0
   *                hardness left, false otherwise.
   */
  public boolean damageComponent(int damage) {
    this.hardnessLeft -= damage;
    return (this.hardnessLeft <= 0);
  }

  /**
   * [setHardnessLeft]
   * Adjusts this component's hardness left to a specific value.
   * @author unknown
   * @param hardnessLeft  The new hardness to be set.
   */
  public void setHardnessLeft(int hardnessLeft) {
    this.hardnessLeft = hardnessLeft;
  }
}