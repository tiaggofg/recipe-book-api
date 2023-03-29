package com.recipe.book.api.services;

import com.mongodb.BasicDBObject;
import com.recipe.book.api.log.Log;
import com.recipe.book.api.repositories.RecipeRepository;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;

import java.io.IOException;
import java.util.List;

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
    public Recipe update(String id, Recipe recipe) {
        return recipeRepository.update(id, recipe);
    }

    @Override
    public Recipe findById(String id) {
        return recipeRepository.findById(id);
    }

    @Override
    public List<Recipe> findByIngredient(String ingredient) {
        return recipeRepository.findByIngredient(ingredient);
    }

    @Override
    public List<Recipe> searchInTitleAndDescription(String search) {
        return recipeRepository.searchInTitleAndDescription(search);
    }

    @Override
    public Recipe create(Recipe recipe) {
        return recipeRepository.create(recipe);
    }

    @Override
    public void delete(String id) {
        recipeRepository.delete(id);
    }

    @Override
    public Recipe addLike(Integer userId, String recipeId) {
        return recipeRepository.addLike(userId, recipeId);
    }

    @Override
    public void removeLike(Integer userId, String recipeId) {
        recipeRepository.removeLike(userId, recipeId);
    }

    @Override
    public Recipe addComment(String recipeId, Comment comment) {
        return recipeRepository.addComment(recipeId, comment);
    }

    @Override
    public void updateComment(String recipeId, String commentId, Comment comment) {
        recipeRepository.updateComment(recipeId, commentId, comment);
    }

    @Override
    public Recipe saveOne(BasicDBObject recipe) {
        try {
            return recipeRepository.saveOne(recipe);
        } catch(IOException e) {
            Log.error(e.getMessage(), RecipeServiceImpl.class, e);
            return null;
        }
    }

    @Override
    public void removeComment(String recipeId, String commentId) {
        recipeRepository.removeComment(recipeId, commentId);
    }
}
