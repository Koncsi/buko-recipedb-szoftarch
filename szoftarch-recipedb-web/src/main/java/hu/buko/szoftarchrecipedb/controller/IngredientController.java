package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.model.Ingredient;
import hu.buko.szoftarchrecipedb.service.IngredientService;
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

@Controller
public class IngredientController {

    private static final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    private IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/ingredients")
    public String initIngredientsPage(@ModelAttribute Ingredient ingredient, Model model) {
        logger.debug("initIngredientsPage called");
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "/ingredients";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/deleteIngredient/{name}")
    public String deleteIngredient(@PathVariable String name, RedirectAttributes redirectAttributes) {
        logger.debug("deleteIngredient called");
        logger.debug("ingredient name: " + name);
        ingredientService.deleteIngredientByName(name);
        redirectAttributes.addFlashAttribute("message", "Hozzávaló törölve: " + name);
        return "redirect:/ingredients";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/addIngredient/")
    public String addIngredient(@ModelAttribute Ingredient ingredient, RedirectAttributes redirectAttributes) {
        logger.debug("addIngredient called");
        ingredientService.addIngredient(ingredient);
        redirectAttributes.addFlashAttribute("message", "Hozzávaló sikeresen hozzáadva: " + ingredient.getName());
        return "redirect:/ingredients";
    }
}
