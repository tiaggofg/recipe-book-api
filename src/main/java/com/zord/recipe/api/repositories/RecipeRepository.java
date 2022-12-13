package com.zord.recipe.api.repositories;

import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.model.Recipe;

import java.util.List;

public interface RecipeRepository {

    List<Recipe> findAll();

    List<Recipe> findByIngredient(String ingredient);

    List<Recipe> searchInTitleAndDescription(String search);

    Recipe update(String id, Recipe recipe);

    Recipe findById(String id);

    Recipe create(Recipe recipe);

    Recipe addLike(Integer userId, String recipeId);

    Recipe addComment(String recipeId, Comment comment);

    Recipe updateComment(String recipeId, String commentId, Comment comment);

    void delete(String id);

    void removeLike(Integer userId, String recipeId);

    void removeComment(String recipeId, String commentId);

}