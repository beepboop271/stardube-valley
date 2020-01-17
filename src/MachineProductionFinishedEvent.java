import java.util.EventObject;

/**
 * [MachineProductionFinishedEvent]
 * 2020-01-13
 * @version 0.1
 * @author Joseph Wang
 */

@SuppressWarnings("serial")
public class MachineProductionFinishedEvent extends EventObject {
  public MachineProductionFinishedEvent(ExtrinsicMachine machine) {
    super(machine);
  } 
}