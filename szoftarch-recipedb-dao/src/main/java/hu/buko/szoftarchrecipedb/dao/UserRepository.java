package hu.buko.szoftarchrecipedb.dao;

import hu.buko.szoftarchrecipedb.model.RecipedbUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<RecipedbUser, String> {
    Optional<RecipedbUser> findByUsername(String username);
}
