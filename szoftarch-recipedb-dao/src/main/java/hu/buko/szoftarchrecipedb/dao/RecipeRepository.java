package hu.buko.szoftarchrecipedb.dao;

import hu.buko.szoftarchrecipedb.model.Category;
import hu.buko.szoftarchrecipedb.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecipeRepository extends  MongoRepository<Recipe, String>{
    void deleteById(String id);
    List<Recipe> findAll();
    List<Recipe> findAllByPendingIsTrue();
    List<Recipe> findAllByPendingIsFalse();
    List<Recipe> findAllByPendingIsFalseAndNameLike(String namePart);
    @Query("{ 'ingredients.name' : {$regex : '^?0$', $options: 'i'}, pending:false}")
    List<Recipe> findAllByPendingIsFalseAndIngredientsContains(String ingredientPart);
    @Query("{ 'categories' : ?0 , pending:false}")
    List<Recipe> findAllByCategoriesContains(String categoryName);
    List<Recipe> findByCategoriesAndPendingIsFalse(Category categorie);
}