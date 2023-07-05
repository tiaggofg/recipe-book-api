package com.recipe.book.api.controllers;

import com.recipe.book.api.config.Routing;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

@Singleton
public class UserRouting extends Routing<UserController> {

    @Inject
    public UserRouting(Javalin app, UserController controller) {
        super(app, controller);
    }

    @Override
    public void bindRoutes() {
        getApp().before(getController()::authenticate);

        getApp().routes(() -> {
            path("/user", () -> {
                post(getController()::postUser);
                path("/{userId}", () -> {
                    get(getController()::getUserById);
                });
                path("/{userName}", () -> {
                    get(getController()::getUserByName);
                    put(getController()::putUserByName);
                    delete(getController()::deleteUserByName);
                });
            });
        });
    }
}
