import java.util.EventObject;

/**
 * [FishingGameEndedEvent]
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class FishingGameEndedEvent extends EventObject {
  private Holdable fishReturned;

  public FishingGameEndedEvent(FishingGame gameEnded) {
    super(gameEnded);
    this.fishReturned = gameEnded.getFishReturned();
  }

  public FishingGame getGameEnded() {
    return (FishingGame)(super.getSource());
  }
  
  public Holdable getFishReturned() {
    return this.fishReturned;
  }
}