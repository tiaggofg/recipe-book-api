package com.zord.recipe.api.repositories;

import java.util.List;

import com.zord.recipe.api.model.Recipe;

public interface RecipeRepository {

	List<Recipe> findAll();
	List<Recipe> findByIngredient(String ingredient);
	List<Recipe> searchInTitleAndDescription(String search);
	Recipe update(String id, Recipe recipe);
	Recipe findById(String id);
	Recipe create(Recipe recipe);
	Recipe addLike(Integer userId, String recipeId);
	void delete(String id);
	void removeLike(Integer userId, String recipeId);
	
}