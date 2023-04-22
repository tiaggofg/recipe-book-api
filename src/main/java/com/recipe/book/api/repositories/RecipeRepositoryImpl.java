package com.recipe.book.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.recipe.book.api.exceptions.ObjectNotFoundException;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Like;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class RecipeRepositoryImpl implements RecipeRepository {

    private final MongoCollection<Recipe> coll;

    public RecipeRepositoryImpl(MongoDatabase database) {
        coll = database.getCollection("recipe", Recipe.class);
    }

    @Override
    public Recipe findUserRecipe(String recipeId, String authorId) {
        Bson filter = Filters.and(eq("_id", recipeId), eq("authorId", authorId));
        Recipe recipe = coll.find(filter).first();
        if (recipe == null) {
            throw new ObjectNotFoundException("Receita id: " + recipeId + " não encontrada!");
        }
        return recipe;
    }

    @Override
    public List<Recipe> findByIngredient(String ingredient, String authorId) {
        List<Recipe> result = new ArrayList<>();
        Bson filter = Filters.and(regex("ingredients", ingredient), eq("authorId", authorId));
        for (Recipe recipe : coll.find(filter)) {
            result.add(recipe);
        }
        if (result.isEmpty()) {
            throw new ObjectNotFoundException("Nenhuma receita encontada!");
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public List<Recipe> searchInTitleAndDescription(String search, String authorId) {
        List<Recipe> result = new ArrayList<>();

        Bson filter = Filters.and(
                or(regex("title", search), regex("description", search)),
                eq("authorId", authorId)
        );

        for (Recipe recipe : coll.find(filter)) {
            result.add(recipe);
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
    public Recipe update(String id, String authorId, Recipe recipe) {
        Bson filter = Filters.and(eq("_id", id), eq("authorId", authorId));
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
    public Recipe addLike(User currentUser, String recipeId) {
        FindOneAndUpdateOptions updateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Bson update = Updates.push("likes", new Like(currentUser.getUsername(), currentUser.getId()));
        Bson filter = Filters.eq("_id", recipeId);

        return coll.findOneAndUpdate(filter, update, updateOptions);
    }

    @Override
    public Recipe removeLike(User currentUser, String recipeId) {
        FindOneAndUpdateOptions updateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Bson update = Updates.pull("likes", new Like(currentUser.getUsername(), currentUser.getId()));
        Bson filter = Filters.eq("_id", recipeId);

        return coll.findOneAndUpdate(filter, update, updateOptions);
    }

    @Override
    public Recipe addComment(String recipeId, Comment comment) {
        //TODO:implementation integration between comment and user
        Recipe recipe = findById(recipeId);
        recipe.getComments().add(comment);
        return null; //update(recipeId, recipe);
    }

    @Override
    public Recipe updateComment(String recipeId, String commentId, Comment comment) {
        comment.setId(commentId);
        removeComment(recipeId, commentId);
        return addComment(recipeId, comment);
    }

    @Override
    public void removeComment(String recipeId, String commentId) {
        //TODO:implementation integration between user and comment
        Recipe recipe = findById(recipeId);
        if (!commentExists(recipe, commentId)) {
            throw new ObjectNotFoundException("Comentário id: " + commentId + " não encontrado!");
        }
        recipe.getComments().removeIf(c -> c.getId().equals(commentId));
        //update(recipeId, recipe);
    }

    @Override
    public List<Recipe> findAllUserRecipe(String userId) {
        List<Recipe> result = new ArrayList<>();
        Bson filter = Filters.eq("authorId", userId);
        for (Recipe recipe : coll.find(filter)) {
            result.add(recipe);
        }
        return result;
    }

    @Override
    public Recipe findById(String recipeId) {
        Bson filter = Filters.eq("_id", recipeId);
        Recipe recipe = coll.find(filter).first();
        if (recipe == null) {
            throw new ObjectNotFoundException("Receita id: " + recipeId + " não encontrada!");
        }
        return recipe;
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