/**
 * Harvestable
 * An interface for anything that can be harvested using a tool.
 * 2019-12-21
 * @version 0.1
 * @author Paula Yuan
 */
public interface Harvestable extends Collectable {
  /**
   * [getRequiredTool]
   * Retrieves the required tool needed for harvesting of this object.
   * @return String, the name of the tool that is needed.
   */
  public String getRequiredTool();
}