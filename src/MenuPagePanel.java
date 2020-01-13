import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * [MenuPagePanel]
 * 2020-01-13
 * @version 0.1
 * @author Candice Zhang
 */
@SuppressWarnings("serial")
public class MenuPagePanel extends JPanel {
  private Player player;
  private int currentPage;
  public MenuPagePanel(Player player) {
    this.player = player;
    this.currentPage = -1;
  }

  @Override
  public void paintComponent(Graphics g) {
    if(this.currentPage == -1) {
      return;
    }
  }

}