package com.recipe.book.api.repositories;

import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;

import java.util.List;

public interface RecipeRepository {

    Recipe findUserRecipe(String id, String authorId);

    List<Recipe> findByIngredient(String ingredient, String authorId);

    List<Recipe> searchInTitleAndDescription(String search, String authorId);

    Recipe update(String id, String authorId, Recipe recipe);

    Recipe create(Recipe recipe);

    Recipe addLike(User author, String recipeId);

    Recipe addComment(String recipeId, Comment comment);

    Recipe updateComment(String recipeId, String commentId, Comment comment);

    void delete(String id);

    void removeLike(User author, String recipeId);

    void removeComment(String recipeId, String commentId);

    List<Recipe> findAllUserRecipe(String userId);
}