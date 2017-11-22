package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.model.Category;
import hu.buko.szoftarchrecipedb.model.Ingredient;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        model.addAttribute("recipes", recipeService.getAcceptedRecipes());
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
        recipe.setCategories(categorizeRecipe(recipe));
        recipeService.addRecipe(recipe);
        logger.info("Recipe has been added to pending list: " + recipe.getName());
        redirectAttributes.addFlashAttribute("message", recipe.getName() + " hozzáadva!");
        return "redirect:/addRecipe";
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
        if (recipeList.isEmpty() && redirectAttributes.getFlashAttributes().get("message")==null) {
            model.addAttribute("message", "Nincs függő recept!");
        }
        model.addAttribute("recipes", recipeList);
        return "pendingRecipes";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/pendingRecipes/accept/{id}")
    public String acceptRecipe(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
        logger.debug("acceptRecipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!checkRecipePending(id, recipe, redirectAttributes)) {
            return "redirect:/pendingRecipes";
        }
        recipe.get().setPending(false);
        recipeService.updateRecipe(recipe.get());
        redirectAttributes.addFlashAttribute("message", "Recept elfogadva: " + recipe.get().getName());
        logger.info("Recept has been accepted: " + recipe.get().getName());
        return "redirect:/pendingRecipes";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/pendingRecipes/refuse/{id}")
    public String refuseRecipe(Model model, @PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
        logger.debug("refuseRecipe called");
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (!checkRecipePending(id, recipe, redirectAttributes)) {
            return "redirect:/pendingRecipes";
        }
        recipeService.deleteRecipeById(id);
        redirectAttributes.addFlashAttribute("message", "Recept elutasítva és törölve: " + recipe.get().getName());
        logger.info("Recipe has been refused and deleted!");
        return "redirect:/pendingRecipes";
    }

    private boolean checkRecipePending(String id, Optional<Recipe> recipe, RedirectAttributes redirectAttributes) {
        if (!recipe.isPresent()) {
            logger.error("No recipe is found with id: " + id);
            redirectAttributes.addFlashAttribute("message", "Recept nem található!");
            return false;
        }
        if (!recipe.get().isPending()) {
            logger.error("Recipe has been already accepted!");
            redirectAttributes.addFlashAttribute("message", "Recept már elfogadásra került egyszer!");
            return false;
        }
        return true;
    }

    /**
     * TODO: Rendesen megcsinanli esetleg aszinkron hivast csinalni belole
     * */
    private List<Category> categorizeRecipe(Recipe recipe){
        List<Category> categories = new ArrayList<>();
        List<Ingredient> ingredients = recipe.getIngredients();

        for(Ingredient ingredient : ingredients){
            String ingredientName = ingredient.getName();
            if(ingredientName.equals("Husi")){
                categories.add(Category.HÚSÉTEL);
            }
            else if(ingredientName.equals("Teszta")){
                categories.add(Category.TÉSZTAÉTEL);
            }

        }
        if(categories.isEmpty()){
            categories.add(Category.KATEGORIZÁLATLAN);
        }

        return categories;

    }

    private List<Recipe> getSameCategoryRecipes(Recipe recipe){
        List<Recipe> sameCategory = new ArrayList<>();
        for(Category category : recipe.getCategories()){
            sameCategory.addAll(recipeService.getSameCategory(category));
        }

        sameCategory.removeAll(Collections.singleton(recipe));

        return sameCategory;
    }
}
