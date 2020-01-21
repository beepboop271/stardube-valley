import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

/**
 * [MenuSystemPanel]
 * A JPanel for graphic display of a menu system.
 * 2019-1-20
 * @version 0.1
 * @author Candice Zhang
 */
@SuppressWarnings("serial")
public class MenuSystemPanel extends JPanel {
  private final Button backToMenuButton = new ButtonWithText(625, 600, 150, 75,
                                                             "Back", new Font("Comic Sans MS", Font.BOLD, 30));
  private final Button startButton = new ButtonWithText(300, 600, 150, 75,
                                                             "Start", new Font("Comic Sans MS", Font.BOLD, 30));
  private final Button loreButton = new ButtonWithText(600, 600, 150, 75,
                                                             "Lore", new Font("Comic Sans MS", Font.BOLD, 30));
  private final Button creditsButton = new ButtonWithText(900, 600, 150, 75,
                                                             "Credits", new Font("Comic Sans MS", Font.BOLD, 30));
  
  private String currentPage;
  private String[] loreText;
  private String[] creditText;
  private BufferedImage mainPageImage;
  private MenuSystemEventListener listener;

  /**
   * [MenuSystemPanel]
   * Constructor for a new MenuSystemPanel.
   * @param width   int, width of the panel.
   * @param height  int, height of the panel.
   * @throws        IOException
   */
  public MenuSystemPanel(int width, int height) throws IOException {
    super();
    this.currentPage = "mainPage";
    this.creditText = new String[]{"Created By: (alphabetized by last name)", "Kevin Qiao", "Joseph Wang", "Paula Yuan", "Candice Zhang"};
    this.listener = new MenuSystemEventListener(this);
    this.addMouseListener(this.listener);
    this.setPreferredSize(new Dimension(width, height));

    try {
      String textToSplit = "";
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/Lore"));
      String lineToRead = input.readLine();
      while (lineToRead != null) {
        textToSplit += lineToRead;
        lineToRead = input.readLine();
      }
      this.loreText = this.splitParagraph(70, textToSplit);
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      this.mainPageImage = ImageIO.read(new File("assets/images/menuSystem/mainPage.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * [paintComponent]
   * Paints the graphics of the world on the screen.
   * @param g The Graphics object that is used to paint graphics.
   */
  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    if (this.currentPage.equals("mainPage")) {
      g.drawImage(this.mainPageImage, 0, 0, null);
      startButton.drawButton(g);
      loreButton.drawButton(g);
      creditsButton.drawButton(g);

    } else if (this.currentPage.equals("lorePage")) {
      g.setColor(new Color(128, 66, 19));
      g.fillRect(0, 0, this.getWidth(), this.getHeight());

      g.setColor(new Color(255, 244, 218));
      g.fillRect(15, 15, this.getWidth()-30, this.getHeight()-30);
    
      backToMenuButton.drawButton(g);

      g.setColor(new Color(103, 35, 0));
      g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
      g.drawString("Lore", (this.getWidth()-g.getFontMetrics().stringWidth("Lore"))/2, 100);
      g.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
      for (int i = 0; i < this.loreText.length; i++) {
        g.drawString(loreText[i], (this.getWidth()-g.getFontMetrics().stringWidth(loreText[i]))/2, 200+i*65);
      }

    } else if (this.currentPage.equals("creditsPage")) {
      g.setColor(new Color(128, 66, 19));
      g.fillRect(0, 0, this.getWidth(), this.getHeight());

      g.setColor(new Color(255, 244, 218));
      g.fillRect(15, 15, this.getWidth()-30, this.getHeight()-30);     
      
      backToMenuButton.drawButton(g);

      g.setColor(new Color(103, 35, 0));
      g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
      g.drawString("Credits", (this.getWidth()-g.getFontMetrics().stringWidth("Lore"))/2, 100);
      g.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
      for (int i = 0; i < this.creditText.length; i++) {
        g.drawString(creditText[i], (this.getWidth()-g.getFontMetrics().stringWidth(creditText[i]))/2, 200+i*65);
      }
    }
  }

  /**
   * [splitParagraph]
   * Split a very long string into lines of at most maxLineWidth characters,
   * and retrieves the splited text in a String array.
   * @author             Kevin Qiao, Candice Zhang
   * @param paragraph    String that represents the string to split.
   * @param maxLineWidth int that represents the width limit.
   * @return             String[], splited text in a String array.
   */
  public String[] splitParagraph(int maxLineWidth, String paragraph) {
    // searching for a space " " to end the line
    int endOffset = -1;
    ArrayList<String> lines = new ArrayList<String>();
    String nextLine = "";
    while(paragraph.length() >= maxLineWidth) {
      nextLine = paragraph.substring(0, maxLineWidth);
      if (nextLine.charAt(nextLine.length()+endOffset) == ' ') {
        lines.add(nextLine.substring(0, nextLine.length()+endOffset));
        paragraph = paragraph.substring(maxLineWidth+endOffset+1);
        endOffset = -1;
      } else {
        --endOffset;
      }
    }
    lines.add(paragraph);
    return lines.toArray(new String[0]);
  }

  /**
   * [getListener]
   * Retrieves the MenuSystemEventListener of this panel.
   * @return MenuSystemEventListener, the MenuSystemEventListener of this panel.
   */
  public MenuSystemEventListener getListener() {
    return this.listener;
  }

  /**
   * [getCurrentPage]
   * Retrieves the current page of this panel.
   * @return String, the current page of this panel.
   */
  public String getCurrentPage() {
    return this.currentPage;
  }

  /**
   * [setCurrentPage]
   * Sets the current page of this panel.
   * @param page A String representing the current page of this panel.
   */
  public void setCurrentPage(String page) {
    this.currentPage = page;
  }
  
  /**
   * [getStartButton]
   * Retrieves the start button of this panel.
   * @return Button, the start button of this panel.
   */
  public Button getStartButton() {
    return startButton;
  }

  /**
   * [getLoreButton]
   * Retrieves the lore button of this panel.
   * @return Button, the lore button of this panel.
   */
  public Button getLoreButton() {
    return loreButton;
  }

  /**
   * [getCreditsButton]
   * Retrieves the credits button of this panel.
   * @return Button, the credits button of this panel.
   */
  public Button getCreditsButton() {
    return creditsButton;
  }

  /**
   * [getBackToMenuButton]
   * Retrieves the back-to-menu button of this panel.
   * @return Button, the back-to-menu button of this panel.
   */
  public Button getBackToMenuButton() {
    return backToMenuButton;
  }

}
