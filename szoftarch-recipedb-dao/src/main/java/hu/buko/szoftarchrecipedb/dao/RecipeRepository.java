package hu.buko.szoftarchrecipedb.dao;

import hu.buko.szoftarchrecipedb.model.Category;
import hu.buko.szoftarchrecipedb.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RecipeRepository extends  MongoRepository<Recipe, String>{
    void deleteById(String id);
    List<Recipe> findAll();
    List<Recipe> findAllByPendingIsTrue();
    List<Recipe> findAllByPendingIsFalse();
    List<Recipe> findByCategoriesAndPendingIsFalse(Category categorie);
}