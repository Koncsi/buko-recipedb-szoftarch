package hu.buko.szoftarchrecipedb.dao;

import hu.buko.szoftarchrecipedb.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
