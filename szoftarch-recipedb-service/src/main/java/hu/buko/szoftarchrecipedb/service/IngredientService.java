package hu.buko.szoftarchrecipedb.service;

import hu.buko.szoftarchrecipedb.dao.IngredientRepository;
import hu.buko.szoftarchrecipedb.exception.IngredientNotFoundException;
import hu.buko.szoftarchrecipedb.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private static final Logger logger = LoggerFactory.getLogger(IngredientService.class);

    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void addIngredient(String ingredientName){
        Ingredient ingredient = new Ingredient(ingredientName);
        try {
            getIngredientByname(ingredientName);
            ingredientRepository.save(ingredient);
        } catch (IngredientNotFoundException e) {
            logger.error(e.getMessage());
        }

    }

    public void addIngredient(Ingredient ingredient){
        ingredientRepository.insert(ingredient);
    }

    public Ingredient getIngredientByname(String name) throws IngredientNotFoundException {
        Optional<Ingredient> ingredient= ingredientRepository.findById(name);
        if (ingredient.isPresent()){
            return ingredient.get();
        }
        else throw new IngredientNotFoundException(name);
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    //db-ben benn marad igy???
    public List<Ingredient> getRemainingIngredients(List<Ingredient> ingredientList){
        List<Ingredient> remainingIngredients = ingredientRepository.findAll();
        remainingIngredients.removeAll(ingredientList);
        return remainingIngredients;
    }

    public void deleteIngredientByName(String name){
        Optional<Ingredient> ingredient = ingredientRepository.findById(name);
        if (ingredient.isPresent()){
            ingredientRepository.delete(ingredient.get());
        }
    }
}
