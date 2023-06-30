package com.recipe.book.api.controllers;

import io.javalin.http.Context;

public interface UserController {

    void getUserById(Context ctx);

    void getUserByName(Context ctx);

    void putUserByName(Context ctx);

    void postUser(Context ctx);

    void deleteUserByName(Context ctx);

    void authenticate(Context context) throws Exception;
}
