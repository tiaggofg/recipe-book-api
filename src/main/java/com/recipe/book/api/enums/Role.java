package com.recipe.book.api.enums;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {

    COMMON_USER,
    ADMIN;
}
