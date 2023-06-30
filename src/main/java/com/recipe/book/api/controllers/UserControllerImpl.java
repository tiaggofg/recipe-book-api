package com.recipe.book.api.controllers;

import com.recipe.book.api.exceptions.InvalidCredentialsException;
import com.recipe.book.api.model.User;
import com.recipe.book.api.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Inject
    public UserControllerImpl(UserService service) {
        this.service = service;
    }

    @Override
    public void authenticate(Context ctx) {
        if (!(ctx.path().equals("/user") && ctx.method() == HandlerType.POST)) {
            BasicAuthCredentials authCredentials = ctx.basicAuthCredentials();
            if (authCredentials == null || !service.isValidCredentials(authCredentials))
                throw new InvalidCredentialsException("Usuário ou senha inválidos!");
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
        User user = service.findByUsername(userName);
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
