package com.recipe.book.api.repositories;

import com.mongodb.BasicDBObject;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;

import java.io.IOException;
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

    Recipe saveOne(BasicDBObject recipe) throws IOException;
}