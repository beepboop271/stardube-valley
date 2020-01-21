import java.util.EventObject;

/**
 * [FishingGameEndedEvent]
 * An event that signals when a fishing game has ended.
 * 2019-12-29
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class FishingGameEndedEvent extends EventObject {
  private Holdable fishReturned;

  /**
   * [FishingGameEndedEvent]
   * Constructor for a new FishingGameEndedEvent.
   * @param gameEnded FishingGame, the ended fishing game that will be processed.
   */
  public FishingGameEndedEvent(FishingGame gameEnded) {
    super(gameEnded);
    this.fishReturned = gameEnded.getFishReturned();
  }

  /**
   * [getGameEnded]
   * Retrieves the ended fishing game.
   * @return FishingGame, the ended fishing game that will be processed.
   */
  public FishingGame getGameEnded() {
    return (FishingGame)(super.getSource());
  }
  
  /**
   * [getFishReturned]
   * Retrieves the returned fish product of the ended fishing game.
   * @return Holdable, the returned fish product of the ended fishing game.
   */
  public Holdable getFishReturned() {
    return this.fishReturned;
  }
}