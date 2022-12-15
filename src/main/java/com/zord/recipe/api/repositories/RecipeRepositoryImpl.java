package com.zord.recipe.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zord.recipe.api.exceptions.ObjectNotFoundException;
import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.model.Recipe;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

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
        Recipe recipe = coll.find(filter).first();
        if (recipe == null) {
            throw new ObjectNotFoundException("Receita id: " + id + " não encontrada!");
        }
        return recipe;
    }

    @Override
    public List<Recipe> findByIngredient(String ingredient) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : coll.find().filter(regex("ingredients", ingredient))) {
            result.add(recipe);
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public List<Recipe> searchInTitleAndDescription(String search) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : coll.find().filter(regex("title", search))) {
            result.add(recipe);
        }
        for (Recipe recipe : coll.find().filter(regex("description", search))) {
            if (!result.contains(recipe)) {
                result.add(recipe);
            }
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public Recipe create(Recipe recipe) {
        String id = new ObjectId().toString();
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
    public Recipe addLike(Integer userId, String recipeId) {
        Recipe recipe = findById(recipeId);
        recipe.getLikes().add(userId);
        update(recipeId, recipe);
        return findById(recipeId);
    }

    @Override
    public void removeLike(Integer userId, String recipeId) {
        Recipe recipe = findById(recipeId);
        recipe.getLikes().remove(userId);
        update(recipeId, recipe);
    }

    @Override
    public Recipe addComment(String recipeId, Comment comment) {
        Recipe recipe = findById(recipeId);
        recipe.getComments().add(comment);
        return update(recipeId, recipe);
    }

    @Override
    public Recipe updateComment(String recipeId, String commentId, Comment comment) {
        comment.setId(commentId);
        removeComment(recipeId, commentId);
        return addComment(recipeId, comment);
    }

    @Override
    public void removeComment(String recipeId, String commentId) {
        Recipe recipe = findById(recipeId);
        recipe.getComments().removeIf(c -> c.getId().equals(commentId));
        update(recipeId, recipe);
    }
}