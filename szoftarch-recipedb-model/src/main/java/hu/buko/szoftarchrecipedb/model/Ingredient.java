package hu.buko.szoftarchrecipedb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Ingredient {
    @Id
    private String name;
    private String quantity;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }
    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
