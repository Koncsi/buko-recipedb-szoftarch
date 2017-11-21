package hu.buko.szoftarchrecipedb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Ingredient {
    @Id
    private String name;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }
}
