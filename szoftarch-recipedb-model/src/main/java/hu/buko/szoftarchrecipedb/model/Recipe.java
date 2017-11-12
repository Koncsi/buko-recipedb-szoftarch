package hu.buko.szoftarchrecipedb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Recipe{
    @Id
    private String id;

    private String name;
    private String description;
    private boolean pending;


    public Recipe(){}

    public Recipe(String name, String description){
        this.name = name;
        this.description = description;
        this.pending = true;
    }
}