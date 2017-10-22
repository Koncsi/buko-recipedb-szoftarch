package hu.buko.szoftarchrecipedb.service;

import hu.buko.szoftarchrecipedb.dao.RecipeRepository;
import hu.buko.szoftarchrecipedb.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public void addRecipe(Recipe recipe){
        recipeRepository.save(recipe);
    }

    public void deleteRecipeById(Long id){recipeRepository.deleteById(id);}
}
