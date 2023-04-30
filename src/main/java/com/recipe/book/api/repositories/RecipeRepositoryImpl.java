package com.recipe.book.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.recipe.book.api.exceptions.ObjectNotFoundException;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Like;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void addComment(String recipeId, Comment comment) {
        FindOneAndUpdateOptions updateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Bson filter = Filters.eq("_id", recipeId);
        Bson update = Updates.push("comments", comment);
        Recipe recipe = coll.findOneAndUpdate(filter, update, updateOptions);
        if (recipe == null) {
            throw new ObjectNotFoundException("Não foi possível adicionar o comentário a receita. Receita id: " + recipeId + " não encontrada!");
        }
    }

    @Override
    public void updateComment(String recipeId, Comment currentComment, Comment newComment) {
        newComment.setId(currentComment.getId());
        newComment.setAuthorId(currentComment.getAuthorId());

        removeComment(recipeId, currentComment);
        addComment(recipeId, newComment);
    }

    @Override
    public void removeComment(String recipeId, Comment comment) {
        Bson filter = Filters.eq("_id", recipeId);
        Bson update = Updates.pull("comments", comment);
        coll.updateOne(filter, update);
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
}