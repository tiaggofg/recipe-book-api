package com.zord.recipe.api.controllers;

import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.model.Recipe;
import com.zord.recipe.api.services.CommentService;
import com.zord.recipe.api.services.RecipeService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class RecipeControllerImpl implements RecipeController {

    private RecipeService recipeService;
    private CommentService commentService;

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
        var id = ctx.pathParam("id");
        ctx.json(recipeService.findById(id)).status(HttpStatus.OK);
    }

    @Override
    public void getByIngredient(Context ctx) {
        var ingredient = ctx.queryParamMap().values().stream().findAny().get().get(0);
        ctx.json(recipeService.findByIngredient(ingredient)).status(HttpStatus.OK);
    }

    @Override
    public void getBySearch(Context ctx) {
        var search = ctx.queryParamMap().values().stream().findAny().get().get(0);
        ctx.json(recipeService.searchInTitleAndDescription(search)).status(HttpStatus.OK);
    }

    @Override
    public void post(Context ctx) {
        var recipe = ctx.bodyAsClass(Recipe.class);
        var recipeCreated = recipeService.create(recipe);
        ctx.json(recipeCreated).status(HttpStatus.CREATED);
    }

    @Override
    public void put(Context ctx) {
        var recipe = ctx.bodyAsClass(Recipe.class);
        var id = ctx.pathParam("id");
        recipeService.update(id, recipe);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Context ctx) {
        var id = ctx.pathParam("id");
        recipeService.delete(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void postLike(Context ctx) {
        var userId = Integer.parseInt(ctx.pathParam("userId"));
        var recipeId = ctx.pathParam("id");
        ctx.json(recipeService.addLike(userId, recipeId)).status(HttpStatus.CREATED);
    }

    @Override
    public void deleteLike(Context ctx) {
        var userId = Integer.parseInt(ctx.pathParam("userId"));
        var recipeId = ctx.pathParam("id");
        recipeService.removeLike(userId, recipeId);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void postComment(Context ctx) {
        var recipeId = ctx.pathParam("id");
        var comment = ctx.bodyAsClass(Comment.class);
        var commentCreated = commentService.create(comment);
        recipeService.addComment(recipeId, commentCreated);
        ctx.json(commentCreated).status(HttpStatus.CREATED);
    }

    @Override
    public void putComment(Context ctx) {
        var recipeId = ctx.pathParam("id");
        var commentId = ctx.pathParam("commentId");
        var comment = ctx.bodyAsClass(Comment.class);
        recipeService.updateComment(recipeId, commentId, comment);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void deleteComment(Context ctx) {
        var recipeId = ctx.pathParam("id");
        var commentId = ctx.pathParam("commentId");
        recipeService.removeComment(recipeId, commentId);
        commentService.delete(commentId);
        ctx.status(HttpStatus.NO_CONTENT);
    }

}