package hu.buko.szoftarchrecipedb;

import hu.buko.szoftarchrecipedb.exception.IngredientNotFoundException;
import hu.buko.szoftarchrecipedb.model.Ingredient;
import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.model.RecipedbUser;
import hu.buko.szoftarchrecipedb.service.IngredientService;
import hu.buko.szoftarchrecipedb.service.RecipeService;
import hu.buko.szoftarchrecipedb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashMap;
import java.util.Map;


@EnableMongoRepositories
@EntityScan
@ComponentScan("hu.buko")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SzoftarchRecipedbApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SzoftarchRecipedbApplication.class);

    private RecipeService recipeService;
    private UserService userService;
    private IngredientService ingredientService;

    @Autowired
    public SzoftarchRecipedbApplication(RecipeService recipeService, UserService userService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.ingredientService = ingredientService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SzoftarchRecipedbApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (ingredientService.getAllIngredients().isEmpty()) {
            ingredientService.addIngredient("vaj");
            ingredientService.addIngredient("kenyér");
        }

        if (recipeService.getAllRecipes().size() == 0) {
            Map<Ingredient, Integer> ingredientIntegerMap = new HashMap<>();
            try {
                ingredientIntegerMap.put(ingredientService.getIngredientByname("vaj"), 10);
            }
            catch (IngredientNotFoundException e){
                logger.info("vaj is already added");
            }
            try {
                ingredientIntegerMap.put(ingredientService.getIngredientByname("kenyér"), 15);
            }
            catch (IngredientNotFoundException e){
                logger.info("kenyér is already added");
            }
            recipeService.addRecipe(new Recipe("Gulyás", "Finom"));
            recipeService.addRecipe(new Recipe("Vajas kenyér", "Azis"));
            recipeService.addRecipe(new Recipe("Pisztácia", "Nem is volt"));
        }

        for (Recipe r : recipeService.getAllRecipes()) {
            logger.debug(r.toString());
        }

        if (userService.getAllUsers().size() == 0) {
            userService.addAdmin("admin", "admin");
            userService.addUser("jani", "jani");
        }

        for (RecipedbUser user : userService.getAllUsers()) {
            logger.debug(user.toString());
        }
    }
}
