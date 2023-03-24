package com.recipe.book.api.controllers;

import com.recipe.book.api.enums.Role;
import com.recipe.book.api.model.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public interface UserController {

    void getUserById(Context ctx);
    void getUserByName(Context ctx);
    void putUser(Context ctx);
    void postUser(Context ctx);
    void deleteUser(Context ctx);
    void authenticate(Handler handler, Context context, Set<? extends RouteRole> routeRoles) throws Exception;
}
