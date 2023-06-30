package com.recipe.book.api.config;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mongodb.MongoSocketOpenException;
import com.recipe.book.api.controllers.RecipeController;
import com.recipe.book.api.controllers.UserController;
import com.recipe.book.api.controllers.UserControllerImpl;
import com.recipe.book.api.exceptions.*;
import com.recipe.book.api.log.Log;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

@Singleton
public class Startup {

    private final Javalin app;
    private final UserController userController;
    private final RecipeController recipeController;

    @Inject
    public Startup(Javalin app, UserController userController, RecipeController recipeController) {
        this.app = app;
        this.userController = userController;
        this.recipeController = recipeController;
    }

    public void boot() {
        app.before(userController::authenticate);

        app.routes(() -> {
            path("/", () -> {
                get(ctx -> ctx.status(HttpStatus.FORBIDDEN));
            });
            path("/authenticate", () -> {
                post(ctx -> ctx.status(200));
            });
            path("/user", () -> {
                post(userController::postUser);
                path("/{userId}", () -> {
                    get(userController::getUserById);
                });
                path("/{userName}", () -> {
                    get(userController::getUserByName);
                    put(userController::putUserByName);
                    delete(userController::deleteUserByName);
                });
            });
            path("recipe", () -> {
                get(recipeController::get);
                post(recipeController::post);
                path("ingredient", () -> {
                    get(recipeController::getByIngredient);
                });
                path("search", () -> {
                    get(recipeController::getBySearch);
                });
                path("{id}", () -> {
                    get(recipeController::getById);
                    put(recipeController::put);
                    delete(recipeController::delete);
                    path("like", () -> {
                        post(recipeController::like);
                        delete(recipeController::dislike);
                    });
                    path("comment", () -> {
                        post(recipeController::addComment);
                        path("{commentId}", () -> {
                            put(recipeController::updateComment);
                            delete(recipeController::removeComment);
                        });
                    });
                });
            });
        });

        app.exception(CommentException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(UserLikedRecipeException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(UserNotLikeRecipeException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(InvalidCredentialsException.class, (e, ctx) -> {
            Log.info("Acesso negado para: " + ctx.ip(), UserControllerImpl.class);
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(MongoSocketOpenException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String errorMessage = "Ocorreu um erro no servidor. Entre em contato com o administrador!";
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(MismatchedInputException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String errorMessage = "Nenhum conteúdo enviado no request body!";
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String errorMessage = "Request body inválido. Envie um JSON conforme específicado na documentação!";
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(ObjectNotFoundException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.NOT_FOUND;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(IdInvalidException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });
    }
}
