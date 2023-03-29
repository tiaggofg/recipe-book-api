package com.recipe.book.api.services;

import com.mongodb.BasicDBObject;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> findAll();

    List<Recipe> findByIngredient(String ingredient);

    List<Recipe> searchInTitleAndDescription(String search);

    Recipe update(String id, Recipe recipe);

    Recipe findById(String id);

    Recipe create(Recipe recipe);

    Recipe addLike(Integer userId, String recipeId);

    Recipe addComment(String recipeId, Comment comment);

    void delete(String id);

    void removeLike(Integer userId, String recipeId);

    void removeComment(String recipeId, String commentId);

    void updateComment(String recipeId, String commentId, Comment comment);

    Recipe saveOne(BasicDBObject recipe);
}