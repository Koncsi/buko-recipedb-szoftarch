package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/")
    public String listRecipes(Model model) {
        logger.debug("listRecipes called");
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "listRecipes";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/deleteRecipe/{id}")
    public String deleteRecipe(@PathVariable String id, RedirectAttributes redirectAttributes) {
        logger.debug("deleteRecipes called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!recipe.isPresent()) {
            logger.error("Recipe is not found: " + recipe.get().getName());
        }
        recipeService.deleteRecipeById(id);
        redirectAttributes.addFlashAttribute("message", recipe.get().getName() + " törölve!");
        return "redirect:/";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/addRecipe")
    public String initAddRecipePage(@ModelAttribute Recipe recipe) {
        return "addRecipe";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/addRecipe")
    public String addRecipe(@ModelAttribute Recipe recipe, RedirectAttributes redirectAttributes) {
        logger.debug("addRecipe called");
        recipeService.addRecipe(recipe);
        logger.info("Recipe has been added: " + recipe.getName());
        redirectAttributes.addFlashAttribute("message", recipe.getName() + " hozzáadva!");
        return "addRecipe";
    }
}
