package hu.buko.szoftarchrecipedb.exception;

public class PendingRecipeException extends Exception {
    public PendingRecipeException(String name) {
        super("Pending went wrong: " + name);
    }
}

