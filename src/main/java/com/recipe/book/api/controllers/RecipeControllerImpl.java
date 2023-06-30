package com.recipe.book.api.controllers;

import com.recipe.book.api.exceptions.CommentException;
import com.recipe.book.api.exceptions.ObjectNotFoundException;
import com.recipe.book.api.model.Comment;
import com.recipe.book.api.model.Recipe;
import com.recipe.book.api.model.User;
import com.recipe.book.api.services.CommentService;
import com.recipe.book.api.services.RecipeService;
import com.recipe.book.api.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class RecipeControllerImpl implements RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final CommentService commentService;

    @Inject
    public RecipeControllerImpl(RecipeService recipeService, UserService userService, CommentService commentService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @Override
    public void get(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findByUsername(username);
        List<Recipe> recipes = recipeService.findAllUserRecipe(recipeOwner.getId());
        ctx.json(recipes).status(HttpStatus.OK);
    }

    @Override
    public void getById(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findByUsername(username);
        String recipeId = ctx.pathParam("id");
        Recipe recipe = recipeService.findUserRecipe(recipeId, recipeOwner.getId());
        ctx.json(recipe).status(HttpStatus.OK);
    }

    @Override
    public void getByIngredient(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findByUsername(username);
        String ingredient = ctx.queryParam("ingredient");
        ctx.json(recipeService.findByIngredient(ingredient, recipeOwner.getId())).status(HttpStatus.OK);
    }

    @Override
    public void getBySearch(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User recipeOwner = userService.findByUsername(username);
        String search = ctx.queryParam("search");
        List<Recipe> recipe = recipeService.searchInTitleAndDescription(search, recipeOwner.getId());
        ctx.json(recipe).status(HttpStatus.OK);
    }

    @Override
    public void post(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User author = userService.findByUsername(username);

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
        User recipeOwner = userService.findByUsername(username);
        String id = ctx.pathParam("id");
        recipeService.update(id, recipeOwner.getId(), recipe);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        String recipeId = ctx.pathParam("id");

        User recipeOwner = userService.findByUsername(username);
        Recipe recipe = recipeService.findUserRecipe(recipeId, recipeOwner.getId());

        if (!recipe.getComments().isEmpty()) {
            recipe.getComments().forEach(c -> commentService.delete(c.getId()));
        }

        recipeService.delete(recipeId);
        userService.removeRecipe(username, recipeId);
        ctx.status(HttpStatus.NO_CONTENT);
    }

    @Override
    public void like(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User currentUser = userService.findByUsername(username);
        String recipeId = ctx.pathParam("id");

        userService.addRecipeToListLike(currentUser, recipeId);
        Recipe recipeLiked = recipeService.addLike(currentUser, recipeId);

        ctx.json(recipeLiked).status(HttpStatus.CREATED);
    }

    @Override
    public void dislike(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User currentUser = userService.findByUsername(username);
        String recipeId = ctx.pathParam("id");

        userService.removeRecipeFromListLike(currentUser, recipeId);
        Recipe recipeDisliked = recipeService.removeLike(currentUser, recipeId);

        ctx.json(recipeDisliked).status(HttpStatus.OK);
    }

    @Override
    public void addComment(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User currentUser = userService.findByUsername(username);
        String recipeId = ctx.pathParam("id");

        Comment comment = ctx.bodyAsClass(Comment.class);
        comment.setAuthorId(currentUser.getId());
        comment.setId(new ObjectId().toString());

        recipeService.addComment(recipeId, comment);
        Comment commentCreated = commentService.create(comment);

        ctx.json(commentCreated).status(HttpStatus.CREATED);
    }

    @Override
    public void updateComment(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User currentUser = userService.findByUsername(username);
        String recipeId = ctx.pathParam("id");
        String commentId = ctx.pathParam("commentId");

        Comment newComment = ctx.bodyAsClass(Comment.class);
        Recipe recipe = recipeService.findById(recipeId);

        Comment currentComment = recipe.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Comentário id: " + commentId + " não encontrado!"));

        if (!currentComment.getAuthorId().equals(currentUser.getId())) {
            throw new CommentException("Comnetário id: " + commentId + " não pertence ao usuário " + currentUser.getUsername());
        }

        recipeService.updateComment(recipeId, currentComment, newComment);
        newComment = commentService.update(commentId, newComment);

        ctx.status(HttpStatus.OK).json(newComment);
    }

    @Override
    public void removeComment(Context ctx) {
        String username = ctx.basicAuthCredentials().getUsername();
        User currentUser = userService.findByUsername(username);

        String recipeId = ctx.pathParam("id");
        String commentId = ctx.pathParam("commentId");

        Recipe recipe = recipeService.findById(recipeId);
        Comment comment = recipe.getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Comentário id: " + commentId + " não encontrado!"));

        if (!comment.getAuthorId().equals(currentUser.getId()) && !recipe.getAuthorId().equals(currentUser.getId())) {
            throw new CommentException("O comentário id: " + commentId + " não pertence ao usuário " + currentUser.getUsername());
        }

        recipeService.removeComment(recipeId, comment);
        commentService.delete(commentId);

        ctx.status(HttpStatus.NO_CONTENT);
    }
}