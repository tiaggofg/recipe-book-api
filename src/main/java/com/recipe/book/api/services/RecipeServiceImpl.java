package com.recipe.book.api.services;

import com.recipe.book.api.exceptions.UserLikedRecipeException;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;
import com.recipe.book.api.repositories.RecipeRepository;

import java.util.List;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe findById(String recipeId) {
        return recipeRepository.findById(recipeId);
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
    public Recipe addLike(User currentUser, String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId);
        boolean recipeContainsLike = recipe.getLikes()
                .stream()
                .anyMatch(like -> like.getUserId().equals(currentUser.getId()) && like.getUsername().equals(currentUser.getUsername()));

        if (recipeContainsLike) {
            throw new UserLikedRecipeException("Usuário id: " + currentUser.getId() + " já curtiu a receita id: " + recipeId + "!");
        }

        return recipeRepository.addLike(currentUser, recipeId);
    }

    @Override
    public Recipe removeLike(User currentUser, String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId);
        boolean recipeNotContainsLike = recipe.getLikes()
                .stream()
                .noneMatch(like -> like.getUserId().equals(currentUser.getId()) && like.getUsername().equals(currentUser.getUsername()));

        if (recipeNotContainsLike) {
            throw new UserLikedRecipeException("Usuário id: " + currentUser.getId() + " não curtiu a receita id: " + recipeId + "!");
        }

        return recipeRepository.removeLike(currentUser, recipeId);
    }

    @Override
    public void addComment(String recipeId, Comment comment) {
        recipeRepository.addComment(recipeId, comment);
    }

    @Override
    public void updateComment(String recipeId, Comment currentComment, Comment newComment) {
        recipeRepository.updateComment(recipeId, currentComment, newComment);
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
    public void removeComment(String recipeId, Comment comment) {
        recipeRepository.removeComment(recipeId, comment);
    }
}
