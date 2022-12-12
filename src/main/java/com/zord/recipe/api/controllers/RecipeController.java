package com.zord.recipe.api.controllers;

import io.javalin.http.Context;

public interface RecipeController {

    void get(Context ctx);

    void getById(Context ctx);

    void getByIngredient(Context ctx);

    void post(Context ctx);

    void put(Context ctx);

    void delete(Context ctx);

    void getBySearch(Context ctx);

    void postLike(Context ctx);

    void deleteLike(Context ctx);

    void postComment(Context ctx);

    void deleteComment(Context ctx);

    void putComment(Context ctx);

}
