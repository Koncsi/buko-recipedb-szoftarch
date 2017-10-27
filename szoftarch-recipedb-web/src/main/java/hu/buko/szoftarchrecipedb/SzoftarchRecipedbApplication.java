package hu.buko.szoftarchrecipedb;

import hu.buko.szoftarchrecipedb.model.Recipe;
import hu.buko.szoftarchrecipedb.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@EntityScan
@ComponentScan("hu.buko")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SzoftarchRecipedbApplication implements ApplicationRunner{

	private RecipeService recipeService;

	@Autowired
	public SzoftarchRecipedbApplication(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SzoftarchRecipedbApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (recipeService.getAllRecipes().size()==0){
			recipeService.addRecipe(new Recipe("Gulyás", "Finom"));
			recipeService.addRecipe(new Recipe("Palacsinta", "Azis"));
			recipeService.addRecipe(new Recipe("Pisztácia", "Nem is volt"));
		}
	}
}
