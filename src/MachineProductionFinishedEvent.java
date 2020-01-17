import java.util.EventObject;

/**
 * [MachineProductionFinishedEvent]
 * An event that signals when a specific machine has finished their production.
 * 2020-01-13
 * @version 0.1
 * @author Joseph Wang
 */

@SuppressWarnings("serial")
public class MachineProductionFinishedEvent extends EventObject {
  /**
   * [MachineProductionFinishedEvent]
   * Constructor for a new MachineProductionFinishedEvent. This needs the machine
   * that is actually producing the item.
   * @param machine The source machine of this event.
   */
  public MachineProductionFinishedEvent(ExtrinsicMachine machine) {
    super(machine);
  } 
}