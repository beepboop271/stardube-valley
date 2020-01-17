import java.io.IOException;

/**
 * [Consumable]
 * A class for anything that can be eaten or consumed.
 * 2019-12-27
 * @version 0.2
 * @author Paula Yuan, Candice Zhang
 */

public class Consumable extends Useable {
  private int healthGain;
  private int energyGain;

  public Consumable(String name, String description, String imagePath,
                    int healthGain, int energyGain) throws IOException {
    super(name, description, imagePath);
    this.healthGain = healthGain;
    this.energyGain = energyGain;
  }
  
  /**
   * [getHealthGain]
   * Retrieves the amount of health that is gained by eating this consumable.
   * @return int, the health gained by eating this.
   */
  public int getHealthGain() {
    return this.healthGain;
  }
  
  /**
   * [getEnergyGain]
   * Retrieves the amount of energy that is gained by eating this consumable.
   * @return int, the energy gained by eating this.
   */
  public int getEnergyGain() {
    return this.energyGain;
  }
}