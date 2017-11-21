package hu.buko.szoftarchrecipedb.exception;

public class IngredientNotFoundException extends Exception {
    public IngredientNotFoundException(String name) {
        super("Ingredient was not found: " + name);
    }
}
