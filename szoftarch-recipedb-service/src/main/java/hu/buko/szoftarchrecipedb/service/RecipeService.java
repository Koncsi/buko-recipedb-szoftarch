package hu.buko.szoftarchrecipedb.service;

import hu.buko.szoftarchrecipedb.dao.RecipeRepository;
import hu.buko.szoftarchrecipedb.model.Category;
import hu.buko.szoftarchrecipedb.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getAcceptedRecipes() {
        return recipeRepository.findAllByPendingIsFalse();
    }

    public List<Recipe> getAcceptedRecipesLike(String namePart) {
        return recipeRepository.findAllByPendingIsFalseAndNameLike(namePart);
    }

    public List<Recipe> getPendingRecipes() {
        return recipeRepository.findAllByPendingIsTrue();
    }

    public void addRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void deleteRecipeById(String id) {
        recipeRepository.deleteById(id);
    }

    public Optional<Recipe> getRecipeById(String id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> getSameCategory(Category category) {
        return recipeRepository.findByCategoriesAndPendingIsFalse(category);
    }
}
