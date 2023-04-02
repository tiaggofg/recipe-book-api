package com.recipe.book.api.controllers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public interface UserController {

    void getUserById(Context ctx);

    void getUserByName(Context ctx);

    void putUserByName(Context ctx);

    void postUser(Context ctx);

    void deleteUserByName(Context ctx);

    void authenticate(Handler handler, Context context, Set<? extends RouteRole> routeRoles) throws Exception;
}
