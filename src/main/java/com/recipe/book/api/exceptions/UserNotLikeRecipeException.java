package com.recipe.book.api.exceptions;

public class UserNotLikeRecipeException extends RuntimeException {

    public UserNotLikeRecipeException(String message) {
        super(message);
    }
}
