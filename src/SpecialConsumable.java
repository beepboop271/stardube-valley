import java.io.IOException;

/**
 * [SpecialConsumable]
 * A consumable that is able to adjust max health or energy.
 * 2019-01-19
 * @version 0.1
 * @author Joseph Wang
 */

public class SpecialConsumable extends Consumable {
  private int maxEnergyGain, maxHealthGain;

  /**
   * [SpecialConsumable]
   * Constructor for a new SpecialConsumable;
   * @param name          The name of this consumable.
   * @param description   The description of this consumable.
   * @param imagePath     The path to this consumable's images.
   * @param healthGain    The amount of health to gain.
   * @param energyGain    The amount of energy to gain.
   * @param maxHealthGain The amount of max health to gain.
   * @param maxEnergyGain The amount of max energy to gain.
   * @throws IOException
   */
  public SpecialConsumable(String name, String description, String imagePath,
                           int healthGain, int energyGain, int maxHealthGain,
                           int maxEnergyGain) throws IOException {
    super(name, description, imagePath, healthGain, energyGain);
    this.maxEnergyGain = maxEnergyGain;
    this.maxHealthGain = maxHealthGain;
  }

  /**
   * [getMaxEnergyGain]
   * Retrieves how much health the player should gain to their max
   * energy by consuming this item.
   * @return int, the amount of max energy to gain.
   */
  public int getMaxEnergyGain() {
    return this.maxEnergyGain;
  }

  /**
   * [getMaxHealthGain]
   * Retrieves how much health the player should gain to their max
   * health by consuming this item.
   * @return int, the amount of max health to gain.
   */
  public int getMaxHealthGain() {
    return this.maxHealthGain;
  }
}