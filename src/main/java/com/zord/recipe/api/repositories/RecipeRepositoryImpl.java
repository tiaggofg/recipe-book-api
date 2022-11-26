package com.zord.recipe.api.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.zord.recipe.api.model.Recipe;

public class RecipeRepositoryImpl implements RecipeRepository {

	private MongoCollection<Recipe> coll;

	public RecipeRepositoryImpl(MongoDatabase database) {
		coll = database.getCollection("recipe", Recipe.class);
	}

	@Override
	public List<Recipe> findAll() {
		List<Recipe> result = new ArrayList<>();
		for (Recipe recipe : coll.find()) {
			result.add(recipe);
		}
		return result;
	}

	@Override
	public Recipe findById(String id) {
		Bson filter = eq("_id", id);
		return coll.find(filter).first();
	}
	
	@Override
	public List<Recipe> findByIngredient(String ingredient) {
		List<Recipe> result = new ArrayList<>();
		for (Recipe recipe : coll.find()) {
			if (recipe.getIngredients().contains(ingredient)) {
				result.add(recipe);
			}
		}
		Collections.sort(result);
		return result;
	}
	
	@Override
	public List<Recipe> searchInTitleAndDescription(String search) {
		List<Recipe> result = new ArrayList<>();
		for (Recipe recipe : coll.find()) {
			String[] wordTitle = recipe.getTitle().split(" ");
			String[] wordDescription = recipe.getDescription().split(" ");
			for (String s : wordTitle) {
				if (s.contains(search) && !result.contains(recipe)) {
					result.add(recipe);
				}
			}
			for (String s : wordDescription) {
				if (s.contains(search) && !result.contains(recipe)) {
					result.add(recipe);
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public Recipe create(Recipe recipe) {
		var id = new ObjectId().toString();
		recipe.setId(id);
		coll.insertOne(recipe);
		return findById(id);
	}

	@Override
	public Recipe update(String id, Recipe recipe) {
		Bson filter = eq("_id", id);
		return coll.findOneAndReplace(filter, recipe);
	}

	@Override
	public void delete(String id) {
		Bson filter = Filters.eq("_id", id);
		coll.deleteOne(filter);
	}
	
	@Override
	public Recipe addLike(Integer userId, String id) {
		Bson filter = Filters.eq("_id", id);
		Bson update = Updates.addToSet("likes", userId);
		return coll.findOneAndUpdate(filter, update);
	}
	
	@Override
	public void removeLike(Integer userId, String recipeId) {
		Bson filter = Filters.eq("_id", recipeId);
		Bson update = Updates.pull("likes", userId);
		coll.findOneAndUpdate(filter, update);
	}
	
}