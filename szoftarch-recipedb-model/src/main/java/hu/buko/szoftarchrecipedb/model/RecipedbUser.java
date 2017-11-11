package hu.buko.szoftarchrecipedb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
public class RecipedbUser {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<String> authorities;

    public RecipedbUser() {
    }
}
