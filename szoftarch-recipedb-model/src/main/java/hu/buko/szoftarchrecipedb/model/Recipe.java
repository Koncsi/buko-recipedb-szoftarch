package hu.buko.szoftarchrecipedb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Recipe{
    @Id
    public String id;

    public String name;
    public String description;

    public Recipe(){}

    public Recipe(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString(){
        return String.format(
                "Recipe[id=%s, name='%s', description='%s']",
                id, name, description);
    }
}