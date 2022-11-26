package com.zord.recipe.api.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
	public Recipe findById(ObjectId id) {
		return coll.find(eq("_id", id)).first();
	}

	@Override
	public Recipe create(Recipe recipe) {
		var id = new ObjectId();
		recipe.setId(id);
		coll.insertOne(recipe);
		return coll.find(eq("id", id)).first();
	}

	@Override
	public Recipe update(ObjectId id, Recipe recipe) {
		return coll.findOneAndReplace(eq("_id", id), recipe);
	}

	@Override
	public void delete(ObjectId id) {
		coll.deleteOne(eq("_id", id));
	}

}