import java.util.EventObject;

@SuppressWarnings("serial")
public class UseableUsedEvent extends EventObject {
  public UseableUsedEvent(Useable source) {
    super(source);
  }

  public Useable getHoldableUsed() {
    return (Useable)super.getSource();
  }
}