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
        getApp().routes(() -> {
            path("recipe", () -> {
                get(getController()::get);
                post(getController()::post);
                path("ingredient", () -> {
                    get(getController()::getByIngredient);
                });
                path("search", () -> {
                    get(getController()::getBySearch);
                });
                path("{id}", () -> {
                    get(getController()::getById);
                    put(getController()::put);
                    delete(getController()::delete);
                    path("like", () -> {
                        post(getController()::like);
                        delete(getController()::dislike);
                    });
                    path("comment", () -> {
                        post(getController()::addComment);
                        path("{commentId}", () -> {
                            put(getController()::updateComment);
                            delete(getController()::removeComment);
                        });
                    });
                });
            });
        });
    }
}
