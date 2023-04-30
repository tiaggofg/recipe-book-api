package com.recipe.book.api.repositories;

import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;

import java.util.List;

public interface RecipeRepository {

    Recipe findUserRecipe(String id, String authorId);

    Recipe findById(String recipeId);

    List<Recipe> findByIngredient(String ingredient, String authorId);

    List<Recipe> searchInTitleAndDescription(String search, String authorId);

    Recipe update(String id, String authorId, Recipe recipe);

    Recipe create(Recipe recipe);

    Recipe addLike(User currentUser, String recipeId);

    Recipe removeLike(User currentUser, String recipeId);

    void addComment(String recipeId, Comment comment);

    void updateComment(String recipeId, Comment currentComment, Comment newComment);

    void delete(String id);

    void removeComment(String recipeId, Comment comment);

    List<Recipe> findAllUserRecipe(String userId);
}