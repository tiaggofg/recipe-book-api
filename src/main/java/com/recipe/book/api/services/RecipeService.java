package com.recipe.book.api.services;

import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;

import java.util.List;

public interface RecipeService {

    List<Recipe> findByIngredient(String ingredient, String authorId);

    List<Recipe> searchInTitleAndDescription(String search, String authorId);

    Recipe update(String id, String authorId, Recipe recipe);

    Recipe create(Recipe recipe);

    Recipe addLike(User author, String recipeId);

    Recipe addComment(String recipeId, Comment comment);

    void delete(String id);

    void removeLike(User author, String recipeId);

    void removeComment(String recipeId, String commentId);

    void updateComment(String recipeId, String commentId, Comment comment);

    Recipe findUserRecipe(String id, String authorId);

    List<Recipe> findAllUserRecipe(String id);
}