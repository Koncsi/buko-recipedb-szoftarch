package hu.buko.szoftarchrecipedb;

import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.model.RecipedbUser;
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


@EnableMongoRepositories
@EntityScan
@ComponentScan("hu.buko")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SzoftarchRecipedbApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SzoftarchRecipedbApplication.class);

    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public SzoftarchRecipedbApplication(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SzoftarchRecipedbApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (recipeService.getAllRecipes().size() == 0) {
            recipeService.addRecipe(new Recipe("Gulyás", "Finom"));
            recipeService.addRecipe(new Recipe("Palacsinta", "Azis"));
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
