package com.zord.recipe.api.controllers;

import com.zord.recipe.api.exceptions.IdInvalidException;
import com.zord.recipe.api.exceptions.ObjectNotFoundException;
import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.model.Recipe;
import com.zord.recipe.api.services.CommentService;
import com.zord.recipe.api.services.RecipeService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class RecipeControllerImpl implements RecipeController {

    private final RecipeService recipeService;
    private final CommentService commentService;

    public RecipeControllerImpl(RecipeService recipeService, CommentService commentService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
    }

    @Override
    public void get(Context ctx) {
        ctx.json(recipeService.findAll()).status(HttpStatus.OK);
    }

    @Override
    public void getById(Context ctx) {
        String id = ctx.pathParam("id");
        ctx.json(recipeService.findById(id)).status(HttpStatus.OK);
    }

    @Override
    public void getByIngredient(Context ctx) {
        String ingredient = ctx.queryParam("ingredient");
        ctx.json(recipeService.findByIngredient(ingredient)).status(HttpStatus.OK);
    }

    @Override
    public void getBySearch(Context ctx) {
        String search = ctx.queryParam("search");
        ctx.json(recipeService.searchInTitleAndDescription(search)).status(HttpStatus.OK);
    }

    @Override
    public void post(Context ctx) {
        Recipe recipe = ctx.bodyAsClass(Recipe.class);
        Recipe recipeCreated = recipeService.create(recipe);
        ctx.json(recipeCreated).status(HttpStatus.CREATED);
    }

    @Override
    public void put(Context ctx) {
        Recipe recipe = ctx.bodyAsClass(Recipe.class);
        String id = ctx.pathParam("id");
        recipeService.update(id, recipe);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Context ctx) {
        String id = ctx.pathParam("id");
        Recipe recipe = recipeService.findById(id);
        if (!recipe.getComments().isEmpty()) {
            recipe.getComments().forEach(c -> commentService.delete(c.getId()));
        }
        recipeService.delete(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void postLike(Context ctx) {
        try {
            Integer userId = Integer.parseInt(ctx.pathParam("userId"));
            String recipeId = ctx.pathParam("id");
            ctx.json(recipeService.addLike(userId, recipeId)).status(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("Id inválido!");
        }
    }

    @Override
    public void deleteLike(Context ctx) {
        try {
            Integer userId = Integer.parseInt(ctx.pathParam("userId"));
            String recipeId = ctx.pathParam("id");
            recipeService.removeLike(userId, recipeId);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("Id inválido!");
        }
    }

    @Override
    public void postComment(Context ctx) {
        String recipeId = ctx.pathParam("id");
        Comment comment = ctx.bodyAsClass(Comment.class);
        Comment commentCreated = commentService.create(comment);
        try {
            recipeService.addComment(recipeId, comment);
        } catch (ObjectNotFoundException e) {
            commentService.delete(commentCreated.getId());
            throw new ObjectNotFoundException(e.getMessage());
        }
        ctx.json(comment).status(HttpStatus.CREATED);
    }

    @Override
    public void putComment(Context ctx) {
        String recipeId = ctx.pathParam("id");
        String commentId = ctx.pathParam("commentId");
        Comment comment = ctx.bodyAsClass(Comment.class);
        recipeService.updateComment(recipeId, commentId, comment);
        commentService.update(commentId, comment);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void deleteComment(Context ctx) {
        String recipeId = ctx.pathParam("id");
        String commentId = ctx.pathParam("commentId");
        recipeService.removeComment(recipeId, commentId);
        commentService.delete(commentId);
        ctx.status(HttpStatus.NO_CONTENT);
    }
}