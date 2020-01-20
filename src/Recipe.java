import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * [Recipe]
 * A recipe for a specified products that stores the product name,
 * required ingredients and their quantities.
 * 2020-01-18
 * @version 0.2
 * @author Candice Zhang
 */

class Recipe {
  private String productName;
  private LinkedHashMap<String, Integer> ingredientQuantities;

  /**
   * [Recipe]
   * Constructor for a new Recipe.
   * @param productName String, the name of the product.
   */
  Recipe(String productName) {
    this.ingredientQuantities = new LinkedHashMap<String, Integer>();
    this.productName = productName;
  }

  /**
   * [getProductName]
   * Retrieves the name of the product.
   * @return String, the name of the product.
   */
  public String getProductName() {
    return this.productName;
  }

  /**
   * [addIngredient]
   * Adds an ingredient and its quantity to this recipe.
   * @param name     String, the name of the ingredient.
   * @param quantity int, the quantity of the ingredient.
   */
  public void addIngredient(String name, int quantity) {
    this.ingredientQuantities.put(name, quantity);
  }

  /**
   * [quantityOf]
   * Retrieves the quantity needed of an ingredient.
   * @param name  String, the name of the ingredient.
   * @return      int, the required quantity of the ingredient.
   */
  public int quantityOf(String name) {
    if (this.ingredientQuantities.containsKey(name)) {
      return this.ingredientQuantities.get(name);
    } else {
      return 0;
    }
  }

  /**
   * [getIngredients]
   * Retrieves all ingredients in this recipe.
   * @return String[], a String array of all ingredients in this recipe.
   */
  public String[] getIngredients() {
    ArrayList<String> keys = new ArrayList<String>(this.ingredientQuantities.keySet());
    return keys.toArray(new String[keys.size()]);
  }
}