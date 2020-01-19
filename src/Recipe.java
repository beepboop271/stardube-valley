import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * [Recipe.java]
 * 2020-01-18
 * @version 0.2
 * @author Candice Zhang
 */

class Recipe {
  private String productName;
  private LinkedHashMap<String, Integer> ingredientQuantities;

  Recipe(String productName) {
    this.productName = productName;
  }

  public String getProductName() {
    return this.productName;
  }

  public void addIngredient(String name, int quantity) {
    this.ingredientQuantities.put(name, quantity);
  }

  public int quantityOf(String name) {
    if (this.ingredientQuantities.containsKey(name)) {
      return this.ingredientQuantities.get(name);
    } else {
      return 0;
    }
  }

  public String[] getIngredients() {
    ArrayList<String> keys = new ArrayList<String>(this.ingredientQuantities.keySet());
    return keys.toArray(new String[keys.size()]);
  }
}