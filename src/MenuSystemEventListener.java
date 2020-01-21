import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * [MenuSystemEventListener]
 * Event listener that processes all key and mouse events in a menu system.
 * 2019-12-19
 * @version 0.1
 * @author Candice Zhang
 */

public class MenuSystemEventListener implements MouseListener {
  private MenuSystemPanel menuPanel;
  private boolean shouldStartGame;

  /**
   * [MenuSystemEventListener]
   * Constructor for a new MenuSystemEventListener.
   * @param menuPanel  The corresponding MenuSystemPanel of the menu.
   */
  MenuSystemEventListener(MenuSystemPanel menuPanel) {
    this.menuPanel = menuPanel;
    this.shouldStartGame = false;
  }

  /**
   * [shouldStartGame]
   * Checks if the menu system should be closed and the actual game should start.
   * @return boolean, true if the actual game should start, false otherwise.
   */
  public boolean shouldStartGame() {
    return this.shouldStartGame;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mousePressed(MouseEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseExited(MouseEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    int mouseX = e.getX(), mouseY = e.getY();
    if (this.menuPanel.getCurrentPage().equals("mainPage") && this.menuPanel.getStartButton().inButton(mouseX, mouseY)) {
      this.shouldStartGame = true;

    } else if (this.menuPanel.getCurrentPage().equals("mainPage") && this.menuPanel.getLoreButton().inButton(mouseX, mouseY)) {
      this.menuPanel.setCurrentPage("lorePage");

    } else if (this.menuPanel.getCurrentPage().equals("mainPage") && this.menuPanel.getCreditsButton().inButton(mouseX, mouseY)) {
      this.menuPanel.setCurrentPage("creditsPage");

    } else if ((this.menuPanel.getCurrentPage().equals("lorePage") || this.menuPanel.getCurrentPage().equals("creditsPage"))
               && this.menuPanel.getBackToMenuButton().inButton(mouseX, mouseY)) {
      this.menuPanel.setCurrentPage("mainPage");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseClicked(MouseEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseEntered(MouseEvent e) {
  }
}