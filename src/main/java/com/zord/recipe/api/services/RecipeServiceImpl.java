package com.zord.recipe.api.services;

import java.util.List;

import org.bson.types.ObjectId;

import com.zord.recipe.api.model.Recipe;
import com.zord.recipe.api.repositories.RecipeRepository;

public class RecipeServiceImpl implements RecipeService {

	private RecipeRepository recipeRepository;
	
	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	@Override
	public List<Recipe> findAll() {
		return recipeRepository.findAll();
	}
	
	@Override
	public Recipe update(ObjectId id, Recipe recipe) {
		return recipeRepository.update(id, recipe);
	}

	@Override
	public Recipe findById(ObjectId id) {
		return recipeRepository.findById(id);
	}

	@Override
	public Recipe create(Recipe recipe) {
		return recipeRepository.create(recipe);
	}

	@Override
	public void delete(ObjectId id) {
		recipeRepository.delete(id);
	}

}
