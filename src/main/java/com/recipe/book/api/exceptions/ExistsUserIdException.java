package com.recipe.book.api.exceptions;

public class ExistsUserIdException extends RuntimeException {

    public ExistsUserIdException(String msg) {
        super(msg);
    }

}
