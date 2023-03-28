package com.recipe.book.api.controllers;

import com.recipe.book.api.exceptions.DefaultError;
import com.recipe.book.api.log.Log;
import com.recipe.book.api.model.User;
import com.recipe.book.api.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import io.javalin.security.RouteRole;

import java.util.Set;

public class UserControllerImpl implements UserController {

    private final UserService service;

    public UserControllerImpl(UserService service) {
        this.service = service;
    }

    @Override
    public void authenticate(Handler handler, Context ctx, Set<? extends RouteRole> routeRoles) {
        try {
            BasicAuthCredentials authCredentials = ctx.basicAuthCredentials();
            if (ctx.path().equals("/user") && ctx.method() == HandlerType.POST) {
                handler.handle(ctx);
            } else if (authCredentials != null && service.isValidCredentials(authCredentials)) {
                handler.handle(ctx);
            } else {
                Log.info("Acesso negado para: " + ctx.req().getRemoteHost(), UserControllerImpl.class);
                ctx.status(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), UserControllerImpl.class, e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.getMessage(), "Ocorreu um erro no servidor!", ctx.path());
            ctx.status(status).json(error);
        }
    }

    @Override
    public void getUserById(Context ctx) {
        String userId = ctx.pathParam("userId");
        User user = service.findUser(userId);
        ctx.status(HttpStatus.OK).json(user);
    }

    @Override
    public void getUserByName(Context ctx) {
        String userName = ctx.pathParam("userName");
        User user = service.findUserByName(userName);
        ctx.status(HttpStatus.OK).json(user);
    }

    @Override
    public void putUserByName(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        String userName = ctx.pathParam("userName");
        User updatedUser = service.updateUser(userName, user);
        ctx.status(HttpStatus.OK).json(updatedUser);
    }

    @Override
    public void postUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        service.registerUser(user);
        ctx.status(HttpStatus.OK);
    }

    @Override
    public void deleteUserByName(Context ctx) {
        String userName = ctx.pathParam("userName");
        service.deleteUser(userName);
        ctx.status(HttpStatus.NO_CONTENT);
    }
}
