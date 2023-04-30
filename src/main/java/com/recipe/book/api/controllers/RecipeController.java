package com.recipe.book.api.controllers;

import io.javalin.http.Context;

public interface RecipeController {

    void get(Context ctx);

    void getById(Context ctx);

    void getByIngredient(Context ctx);

    void post(Context ctx);

    void put(Context ctx);

    void delete(Context ctx);

    void getBySearch(Context ctx);

    void like(Context ctx);

    void dislike(Context ctx);

    void addComment(Context ctx);

    void removeComment(Context ctx);

    void updateComment(Context ctx);

}
