package com.zord.recipe.api.repositories;

import java.util.List;

import org.bson.types.ObjectId;

import com.zord.recipe.api.model.Recipe;

public interface RecipeRepository {

	List<Recipe> findAll();
	Recipe update(ObjectId id, Recipe recipe);
	Recipe findById(ObjectId id);
	Recipe create(Recipe recipe);
	void delete(ObjectId id);
	
}