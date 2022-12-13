package com.zord.recipe.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.model.Recipe;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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