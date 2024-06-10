package com.recipe.book.api.controllers;

import com.recipe.book.api.config.Routing;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

@Singleton
public class RecipeRouting extends Routing<RecipeController> {

    @Inject
    public RecipeRouting(Javalin app, RecipeController controller) {
        super(app, controller);
    }

    @Override
    public void bindRoutes() {
        Javalin app = getApp();
        RecipeController controller = getController();

        app.routes(() -> {
            path("/status", () -> {
                get(ctx -> ctx.status(200));
            });
            path("recipe", () -> {
                get(controller::get);
                post(controller::post);
                path("ingredient", () -> {
                    get(controller::getByIngredient);
                });
                path("search", () -> {
                    get(controller::getBySearch);
                });
                path("{id}", () -> {
                    get(controller::getById);
                    put(controller::put);
                    delete(controller::delete);
                    path("like", () -> {
                        post(controller::like);
                        delete(controller::dislike);
                    });
                    path("comment", () -> {
                        post(controller::addComment);
                        path("{commentId}", () -> {
                            put(controller::updateComment);
                            delete(controller::removeComment);
                        });
                    });
                });
            });
        });
    }
}
