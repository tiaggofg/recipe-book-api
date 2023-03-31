package com.recipe.book.api.services;

import com.recipe.book.api.repositories.RecipeRepository;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;

import java.util.List;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe update(String id, String authorId, Recipe recipe) {
        return recipeRepository.update(id, authorId, recipe);
    }

    @Override
    public List<Recipe> findByIngredient(String ingredient, String authorId) {
        return recipeRepository.findByIngredient(ingredient, authorId);
    }

    @Override
    public List<Recipe> searchInTitleAndDescription(String search, String authorId) {
        return recipeRepository.searchInTitleAndDescription(search, authorId);
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
    public Recipe addLike(String authorId, String recipeId) {
        return recipeRepository.addLike(authorId, recipeId);
    }

    @Override
    public void removeLike(String authorId, String recipeId) {
        recipeRepository.removeLike(authorId, recipeId);
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
    public Recipe findUserRecipe(String recipeId, String authorId) {
        return recipeRepository.findUserRecipe(recipeId, authorId);
    }

    @Override
    public List<Recipe> findAllUserRecipe(String userId) {
        return recipeRepository.findAllUserRecipe(userId);
    }

    @Override
    public void removeComment(String recipeId, String commentId) {
        recipeRepository.removeComment(recipeId, commentId);
    }
}
