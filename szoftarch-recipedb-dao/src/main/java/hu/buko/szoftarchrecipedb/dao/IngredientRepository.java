package hu.buko.szoftarchrecipedb.dao;

import hu.buko.szoftarchrecipedb.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {

}
