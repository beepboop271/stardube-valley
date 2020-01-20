import java.util.EventObject;

/**
 * [autoMovementEvent] 2020-01-19
 * 
 * @version 0.1
 * @author Paula Yuan
 */

@SuppressWarnings("serial")
public class AutoMovementEvent extends EventObject {
  private NPC npc;

  public AutoMovementEvent(NPC npc) {
    super(npc);
    this.npc = npc;
  }

  public NPC getNPC() {
    return this.npc;
  }
}