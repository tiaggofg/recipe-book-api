package com.recipe.book.api.services;

import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;

import java.util.List;

public interface RecipeService {

    Recipe findById(String recipeId);

    List<Recipe> findByIngredient(String ingredient, String authorId);

    List<Recipe> searchInTitleAndDescription(String search, String authorId);

    Recipe update(String id, String authorId, Recipe recipe);

    Recipe create(Recipe recipe);

    Recipe addLike(User currentUser, String recipeId);

    Recipe removeLike(User currentUser, String recipeId);

    void addComment(String recipeId, Comment comment);

    void removeComment(String recipeId, Comment comment);

    void delete(String id);

    void updateComment(String recipeId, Comment currentComment, Comment newComment);

    Recipe findUserRecipe(String id, String authorId);

    List<Recipe> findAllUserRecipe(String id);
}