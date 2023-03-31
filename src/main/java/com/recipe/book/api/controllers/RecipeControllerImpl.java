package com.recipe.book.api.controllers;

import com.recipe.book.api.exceptions.IdInvalidException;
import com.recipe.book.api.exceptions.ObjectNotFoundException;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;
import com.recipe.book.api.services.CommentService;
import com.recipe.book.api.services.RecipeService;
import com.recipe.book.api.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class RecipeControllerImpl implements RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final CommentService commentService;

    public RecipeControllerImpl(RecipeService recipeService, UserService userService, CommentService commentService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @Override
    public void get(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findUserByName(username);
        List<Recipe> recipes = recipeService.findAllUserRecipe(recipeOwner.getId());
        ctx.json(recipes).status(HttpStatus.OK);
    }

    @Override
    public void getById(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findUserByName(username);
        String recipeId = ctx.pathParam("id");
        Recipe recipe = recipeService.findUserRecipe(recipeId, recipeOwner.getId());
        ctx.json(recipe).status(HttpStatus.OK);
    }

    @Override
    public void getByIngredient(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findUserByName(username);
        String ingredient = ctx.queryParam("ingredient");
        ctx.json(recipeService.findByIngredient(ingredient, recipeOwner.getId())).status(HttpStatus.OK);
    }

    @Override
    public void getBySearch(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findUserByName(username);
        String search = ctx.queryParam("search");
        List<Recipe> recipe = recipeService.searchInTitleAndDescription(search, recipeOwner.getId());
        ctx.json(recipe).status(HttpStatus.OK);
    }

    @Override
    public void post(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User author = userService.findUserByName(username);

        Recipe recipe = ctx.bodyAsClass(Recipe.class);
        recipe.setAuthorId(author.getId());
        Recipe createdRecipe = recipeService.create(recipe);

        userService.addRecipe(username, createdRecipe.getId());
        ctx.json(createdRecipe).status(HttpStatus.CREATED);
    }

    @Override
    public void put(Context ctx) {
        Recipe recipe = ctx.bodyAsClass(Recipe.class);
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findUserByName(username);
        String id = ctx.pathParam("id");
        recipeService.update(id, recipeOwner.getId(), recipe);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        String recipeId = ctx.pathParam("id");

        User recipeOwner = userService.findUserByName(username);
        Recipe recipe = recipeService.findUserRecipe(recipeId, recipeOwner.getId());

        if (!recipe.getComments().isEmpty()) {
            recipe.getComments().forEach(c -> commentService.delete(c.getId()));
        }

        recipeService.delete(recipeId);
        userService.removeRecipe(username, recipeId);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void postLike(Context ctx) {
        try {
            //TODO:integration between like and user
            String username = ctx.basicAuthCredentials().getUsername();
            User userThatLiked = userService.findUserByName(username);
            String recipeId = ctx.pathParam("id");
            ctx.json(recipeService.addLike(userThatLiked.getId(), recipeId)).status(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("Id inválido!");
        }
    }

    @Override
    public void deleteLike(Context ctx) {
        try {
            String username = ctx.basicAuthCredentials().getUsername();
            String userId = userService.findUserByName(username).getId();
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