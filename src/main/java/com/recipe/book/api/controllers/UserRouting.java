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
        Javalin app = getApp();
        UserController controller = getController();

        app.before(controller::authenticate);

        app.routes(() -> {
            path("/user", () -> {
                post(controller::postUser);
                path("/{userId}", () -> {
                    get(controller::getUserById);
                });
                path("/{userName}", () -> {
                    get(controller::getUserByName);
                    put(controller::putUserByName);
                    delete(controller::deleteUserByName);
                });
            });
        });
    }
}
