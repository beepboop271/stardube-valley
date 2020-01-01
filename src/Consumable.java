import java.io.IOException;

/**
 * [Consumable]
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
  
  public int getHealthGain() {
    return this.healthGain;
  }
  
  public int getEnergyGain() {
    return this.energyGain;
  }
}