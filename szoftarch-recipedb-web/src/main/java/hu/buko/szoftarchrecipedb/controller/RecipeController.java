package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(value = "/")
    public String listRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "listRecipes";
    }

    @GetMapping(value = "/deleteRecipe/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipeById(id);
        return "redirect:/";
    }

    @GetMapping(value = "/addRecipe")
    public String initAddRecipePage(@ModelAttribute Recipe recipe) {
        return "addRecipe";
    }

    @PostMapping(value = "/addRecipe")
    public String addRecipe(@ModelAttribute Recipe recipe) {
        recipeService.addRecipe(recipe);
        return "addRecipe";
    }
}
