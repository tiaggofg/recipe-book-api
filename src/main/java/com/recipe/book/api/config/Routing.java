package com.recipe.book.api.config;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mongodb.MongoSocketOpenException;
import com.recipe.book.api.controllers.UserControllerImpl;
import com.recipe.book.api.exceptions.*;
import com.recipe.book.api.log.Log;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public abstract class Routing<T> {

    private final Javalin app;
    private final T controller;

    public Routing(Javalin app, T controller) {
        this.app = app;
        this.controller = controller;
        addExceptions();
    }

    public Javalin getApp() {
        return app;
    }

    public T getController() {
        return controller;
    }

    public abstract void bindRoutes();

    private void addExceptions() {
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

        Javalin exception = app.exception(InvalidCredentialsException.class, (e, ctx) -> {
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
