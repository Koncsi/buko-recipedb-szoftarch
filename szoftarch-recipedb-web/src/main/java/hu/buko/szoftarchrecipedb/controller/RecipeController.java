package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.exception.PendingRecipeException;
import hu.buko.szoftarchrecipedb.model.Category;
import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.service.CategorizerService;
import hu.buko.szoftarchrecipedb.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private RecipeService recipeService;
    private CategorizerService categorizerService;

    private static final String RECIPES = "recipes";
    private static final String CATEGORIES = "categories";
    private static final String LISTRECIPES = "listRecipes";
    private static final String MESSAGE = "message";

    @Autowired
    public RecipeController(RecipeService recipeService, CategorizerService categorizerService) {
        this.categorizerService = categorizerService;
        this.recipeService = recipeService;
    }

    @GetMapping(value = "/")
    public String listRecipes(Model model) {
        logger.debug("listRecipes called");
        model.addAttribute(RECIPES, recipeService.getAcceptedRecipes());
        model.addAttribute(CATEGORIES, Category.values());
        return LISTRECIPES;
    }

    @PostMapping(value = "/searchRecipe/{namePart}")
    public String searchRecipe(@PathVariable String namePart, Model model) {
        logger.debug("searchRecipe called: " + namePart);
        List<Recipe> recipes = recipeService.getAcceptedRecipesLike(namePart);
        model.addAttribute(RECIPES, recipes);
        model.addAttribute(CATEGORIES, Category.values());
        model.addAttribute("searchedByName", namePart);
        if (recipes.isEmpty())
            model.addAttribute(MESSAGE, "Nem található ilyen recept!");
        return LISTRECIPES;
    }

    @PostMapping(value = "/searchRecipe/ingredient/{ingredient}")
    public String searchRecipeByIngredient(@PathVariable String ingredient, Model model) {
        logger.debug("searchRecipeByIngredient called: " + ingredient);
        List<Recipe> recipes = recipeService.getAcceptedRecipesWihtIngredient(ingredient);
        model.addAttribute(RECIPES, recipes);
        model.addAttribute(CATEGORIES, Category.values());
        model.addAttribute("searchedByIngredient", ingredient);
        if (recipes.isEmpty())
            model.addAttribute(MESSAGE, "Nem található ilyen hozzávalójú recept!");
        return LISTRECIPES;
    }

    @PostMapping(value = "/searchRecipe/category/")
    public String searchRecipeByCategory(@RequestParam String categoryName, Model model) {
        logger.debug("searchRecipeByCategory called: " + categoryName);
        String category = Category.from(categoryName).name();
        List<Recipe> recipes = recipeService.getAcceptedRecipesWithCategory(category);
        model.addAttribute(RECIPES, recipes);
        model.addAttribute(CATEGORIES, Category.values());
        model.addAttribute("searchedByCategory", categoryName);
        if (recipes.isEmpty())
            model.addAttribute(MESSAGE, "Nem található ilyen kategóriájú recept!");
        return LISTRECIPES;
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
        redirectAttributes.addFlashAttribute(MESSAGE, recipe.get().getName() + " törölve!");
        return "redirect:/";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/addRecipe")
    public String initAddRecipePage(@ModelAttribute Recipe recipe) {
        return "addRecipe";
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/addRecipe")
    public String addRecipe(@ModelAttribute Recipe recipe, RedirectAttributes redirectAttributes) {
        logger.debug("addRecipe called");
        recipe.setPending(true);
        recipe.setCategories(Arrays.asList(Category.UNKNOWN));
        recipeService.addRecipe(recipe);
        categorizerService.categorize(recipe);
        logger.info("Recipe has been added to pending list: " + recipe.getName());
        redirectAttributes.addFlashAttribute(MESSAGE, recipe.getName() + " hozzáadva!");
        return "redirect:/addRecipe";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/updateRecipe/{id}")
    public String editRecipe(@ModelAttribute Recipe recipe, RedirectAttributes redirectAttributes) {
        logger.debug("updateRecipe called");
        recipe.setCategories(Arrays.asList(Category.UNKNOWN));
        recipeService.updateRecipe(recipe);
        categorizerService.categorize(recipe);
        redirectAttributes.addFlashAttribute(MESSAGE, recipe.getName() + " modositva!");
        return "redirect:/";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/editRecipe/{id}")
    public String editRecipe(@PathVariable String id, Model model) {
        logger.debug("editRecipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        model.addAttribute("details", recipe.get());
        return "editRecipe";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/recipe/{id}")
    public String addRecipe(@PathVariable String id, Model model) {
        logger.debug("recipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        List<Recipe> similarRecipes = getSameCategoryRecipes(recipe.get());

        model.addAttribute("recipe", recipe.get());
        model.addAttribute("similars", similarRecipes);
        return "recipe";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/pendingRecipes")
    public String initPendingRecipesPage(Model model, RedirectAttributes redirectAttributes) {
        List<Recipe> recipeList = recipeService.getPendingRecipes();
        if (recipeList.isEmpty() && redirectAttributes.getFlashAttributes().get("message") == null) {
            model.addAttribute(MESSAGE, "Nincs függő recept!");
        }
        model.addAttribute(RECIPES, recipeList);
        return "pendingRecipes";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/pendingRecipes/accept/{id}")
    public String acceptRecipe(@PathVariable String id, RedirectAttributes redirectAttributes) throws PendingRecipeException {
        logger.debug("acceptRecipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!checkRecipePending(id, recipe, redirectAttributes)) {
            return "redirect:/pendingRecipes";
        }
        recipe.get().setPending(false);
        recipeService.updateRecipe(recipe.get());
        redirectAttributes.addFlashAttribute(MESSAGE, "Recept elfogadva: " + recipe.get().getName());
        logger.info("Recept has been accepted: " + recipe.get().getName());
        return "redirect:/pendingRecipes";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/pendingRecipes/refuse/{id}")
    public String refuseRecipe(@PathVariable String id, RedirectAttributes redirectAttributes) throws PendingRecipeException {
        logger.debug("refuseRecipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!checkRecipePending(id, recipe, redirectAttributes)) {
            return "redirect:/pendingRecipes";
        }
        recipeService.deleteRecipeById(id);
        redirectAttributes.addFlashAttribute(MESSAGE, "Recept elutasítva és törölve: " + recipe.get().getName());
        logger.info("Recipe has been refused and deleted!");
        return "redirect:/pendingRecipes";
    }

    private boolean checkRecipePending(String id, Optional<Recipe> recipe, RedirectAttributes redirectAttributes) {
        if (!recipe.isPresent()) {
            logger.error("No recipe is found with id: " + id);
            redirectAttributes.addFlashAttribute(MESSAGE, "Recept nem található!");
            return false;
        }
        if (!recipe.get().isPending()) {
            logger.error("Recipe has been already accepted!");
            redirectAttributes.addFlashAttribute(MESSAGE, "Recept már elfogadásra került egyszer!");
            return false;
        }
        return true;
    }

    private List<Recipe> getSameCategoryRecipes(Recipe recipe) {
        List<Recipe> sameCategory = new ArrayList<>();
        Set<Recipe> duplicationReducer = new HashSet<>();

        for (Category category : recipe.getCategories()) {
            duplicationReducer.addAll(recipeService.getSameCategory(category));
        }

        duplicationReducer.removeAll(Collections.singleton(recipe));
        sameCategory.addAll(duplicationReducer);

        return sameCategory;
    }
}
