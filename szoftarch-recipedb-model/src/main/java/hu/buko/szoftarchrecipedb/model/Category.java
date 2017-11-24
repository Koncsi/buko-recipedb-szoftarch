package hu.buko.szoftarchrecipedb.model;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    MEATDISH("Húsfélék"),
    SWEETNESS("Édességek"),
    SOUP("Levesek"),
    PASTA("Tésztafélék"),
    VEGA("Vegetáriánus"),
    UNKNOWN("Egyéb");

    private final String name;

    private Category(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<String> names() {
        List<String> nameList = new ArrayList<>();
        for (Category category: values()) {
            nameList.add(category.toString());
        }
        return nameList;
    }

    public static Category from(String name){
        for (Category category: values()) {
            if (category.toString().equals(name)){
                return category;
            }
        }
        return null;
    }
}
