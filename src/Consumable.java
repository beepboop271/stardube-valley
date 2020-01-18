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

  /**
   * [Consumable]
   * Constructor for a new Consumable
   * @param name The name of this consumable.
   * @param description A description about this consumable.
   * @param imagePath The path to the images related to this consumable.
   * @param healthGain The amount of health gained when consuming.
   * @param energyGain The amount of energy gained when consuming.
   * @throws IOException
   */
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