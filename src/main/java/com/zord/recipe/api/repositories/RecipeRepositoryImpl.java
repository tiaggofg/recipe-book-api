package com.zord.recipe.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zord.recipe.api.exceptions.ExistsUserIdException;
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

    private final MongoCollection<Recipe> coll;

    public RecipeRepositoryImpl(MongoDatabase database) {
        coll = database.getCollection("recipe", Recipe.class);
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : coll.find()) {
            result.add(recipe);
        }
        if (result.isEmpty()) {
            throw new ObjectNotFoundException("Nenhuma receita encontada!");
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
        if (result.isEmpty()) {
            throw new ObjectNotFoundException("Nenhuma receita encontada!");
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
        if (result.isEmpty()) {
            throw new ObjectNotFoundException("Nenhuma receita encontada!");
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
        Recipe updatedRecipe = coll.findOneAndReplace(filter, recipe);
        if (updatedRecipe == null) {
            throw new ObjectNotFoundException("Não foi possível atualizar a receita. Id: " + id + " inexistente!");
        }
        return updatedRecipe;
    }

    @Override
    public void delete(String id) {
        Bson filter = Filters.eq("_id", id);
        if (coll.find(filter).first() == null) {
            throw new ObjectNotFoundException("Não foi possível deletar a receita. Id: " + id + " inexistente!");
        }
        coll.deleteOne(filter);
    }

    @Override
    public Recipe addLike(Integer userId, String recipeId) {
        Recipe recipe = findById(recipeId);
        if (recipe.getLikes().contains(userId)) {
            throw new ExistsUserIdException("Usuário id: " + userId + " já curtiu a receita id: " + recipeId + "!");
        }
        recipe.getLikes().add(userId);
        update(recipeId, recipe);
        return findById(recipeId);
    }

    @Override
    public void removeLike(Integer userId, String recipeId) {
        Recipe recipe = findById(recipeId);
        if (!recipe.getLikes().contains(userId)) {
            throw new ObjectNotFoundException("O usuário id: " + userId + " não curtiu a receita id: " + recipeId + "!");
        }
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
        if (!commentExists(recipe, commentId)) {
            throw new ObjectNotFoundException("Comentário id: " + commentId + " não encontrado!");
        }
        recipe.getComments().removeIf(c -> c.getId().equals(commentId));
        update(recipeId, recipe);
    }

    private boolean commentExists(Recipe recipe, String commentId) {
        List<Comment> comments = recipe.getComments();
        for (Comment c : comments) {
            if (c.getId().equals(commentId)) {
                return true;
            }
        }
        return false;
    }
}