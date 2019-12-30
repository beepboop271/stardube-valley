import java.util.EventObject;

/**
 * [HoldableStackGainedEvent]
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class HoldableStackGainedEvent extends EventObject {

  public HoldableStackGainedEvent(HoldableStack holdableStackGained) {
    super(holdableStackGained);
  }

  public HoldableStack getHoldableStackGained() {
    return (HoldableStack)(super.getSource());
  }
  
}