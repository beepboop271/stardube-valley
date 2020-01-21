import java.io.IOException;
import javax.swing.JFrame;

/**
 * [MenuSystemFrame]
 * The JFrame that holds the menu system panel.
 * 2020-01-20
 * @version 0.1
 * @author Candice Zhang
 */

@SuppressWarnings("serial")
public class MenuSystemFrame extends JFrame {

  /**
   * [MenuSystemFrame]
   * Constructor for a new MenuSystemFrame to display a menu system.
   * @throws IOException
   */
  public MenuSystemFrame() throws IOException {
    super("Stardube Valley");
    this.setContentPane(new MenuSystemPanel(1408, 768));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  /**
   * [refresh]
   * Repaints the screen.
   */
  public void refresh() {
    this.repaint();
  }
}