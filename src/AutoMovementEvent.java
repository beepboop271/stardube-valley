import java.util.EventObject;

/**
 * [AutoMovementEvent]
 * An event to handle automated NPC movements. 
 * 2020-01-19
 * @version 0.1
 * @author Paula Yuan
 */

@SuppressWarnings("serial")
public class AutoMovementEvent extends EventObject {
  private NPC npc;
  /**
   * [AutoMovementEvent]
   * Constructor for a new AutoMovementEvent.
   * @param npc The NPC to be moving
   */
  public AutoMovementEvent(NPC npc) {
    super(npc);
    this.npc = npc;
  }

  /**
   * [getNPC]
   * Returns the NPC that is moving.
   * @return NPC, this NPC.
   */
  public NPC getNPC() {
    return this.npc;
  }
}