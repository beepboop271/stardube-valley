import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * [CraftingStore]
 * An IntrinsicTileComponent that stores essential data for shop interations,
 * including items it can craft and the corresponding recipes.
 * 2020-01-20
 * @version 0.2
 * @author Candice Zhang
 */

public class CraftingStore extends IntrinsicTileComponent implements Drawable {
  private LinkedHashMap<String, Recipe> craftingRecipes;
  private final String[] items;
  
  /**
   * [CraftingStore]
   * Constructor for a new CraftingStore.
   * @param name        Name of the CraftingStore.
   * @param imagePath   Image path of the CraftingStore.
   * @param offsets     The offsets (in tiles) that should be considered during drawing.
   * @param recipesPath The path for the recipes of this CraftingStore.
   * @throws IOException
   */
  public CraftingStore(String name, String imagesPath, double[] offsets, String recipesPath) throws IOException {
    super(name, imagesPath, offsets);
    this.craftingRecipes = new LinkedHashMap<String, Recipe>();
    try {
      BufferedReader input = new BufferedReader(new FileReader("assets/gamedata/"+recipesPath));
      String lineToRead = input.readLine();
      String[] nextLineData;
      while (!(lineToRead.equals("end"))) {
        nextLineData = lineToRead.split("\\s+");
        String productName = nextLineData[0];
        String[] ingredientData;
        Recipe recipeToPut = new Recipe(productName);
        for (int i = 0; i < Integer.parseInt(nextLineData[1]); i++) {
          ingredientData = input.readLine().split("\\s+");
          recipeToPut.addIngredient(ingredientData[0], Integer.parseInt(ingredientData[1]));
        }
        if(nextLineData.length > 2) {
          recipeToPut.setPrice(Double.parseDouble(nextLineData[2]));
        }
        
        this.craftingRecipes.put(productName, recipeToPut);
        input.readLine();
        lineToRead = input.readLine();
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    ArrayList<String> keys = new ArrayList<String>(this.craftingRecipes.keySet());
    this.items = keys.toArray(new String[keys.size()]);
    
  }

  /**
   * [hasItem]
   * Checks if the crafting store can craft the given item.
   * @param item Name of the item.
   * @return boolean, true if the crafting store can craft the given item,
   *         false otherwise.
   */
  public boolean hasItem(String item) {
    return this.craftingRecipes.containsKey(item);
  }

  /**
   * [getItems]
   * Retrieves all items this crafting store can craft.
   * @return String[], a String array containing all items this crafting store craft.
   */
  public String[] getItems() {
    return this.items;
  }

  /**
   * [recipeOf]
   * Retrieves the recipe of the given item.
   * @param item The name of the item.
   * @return Recipe, the recipe of the item.
   */
  public Recipe recipeOf(String item) {
    return this.craftingRecipes.get(item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getImage() {
    return this.getImages()[0];
  }
}