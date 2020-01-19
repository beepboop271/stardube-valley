import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * [CraftingMachine]
 * 2020-01-14
 * @version 0.2
 * @author Candice Zhang
 */

public class CraftingMachine {
  private LinkedHashMap<String, Recipe> recipes;
  private final String[] products;
  
  CraftingMachine(String recipesPath) throws IOException {
    this.recipes = new LinkedHashMap<String, Recipe>();
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
        this.recipes.put(productName, recipeToPut);
        input.readLine();
        lineToRead = input.readLine();
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    ArrayList<String> keys = new ArrayList<String>(this.recipes.keySet());
    this.products = keys.toArray(new String[keys.size()]);
  }

  public boolean hasProduct(String product) {
    return this.recipes.containsKey(product);
  }

  public String[] ingredientsOf(String product) {
    if (this.hasProduct(product)) {
      return this.recipes.get(product).getIngredients();
    }
    return new String[0];
  }

  public String[] getProducts() {
    return this.products;
  }

  public Recipe recipeOf(String product) {
    return this.recipes.get(product);
  }
}