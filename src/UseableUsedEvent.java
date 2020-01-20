import java.util.EventObject;

/**
 * [UseableUsedEvent]
 * An event that signals when a useable has been used.
 * 2019-12-19
 * @version 0.1
 * @author Kevin Qiao
 */

@SuppressWarnings("serial")
public class UseableUsedEvent extends EventObject {
  /**
   * [UseableUsedEvent]
   * Constructor for a new UseableUsedEvent
   * @param source The useable that was used.
   */
  public UseableUsedEvent(Useable source) {
    super(source);
  }

  /**
   * [getHoldableUsed]
   * Retrieves the holdable used in this event.
   * @return Useable, the useable used.
   */
  public Useable getHoldableUsed() {
    return (Useable)super.getSource();
  }
}