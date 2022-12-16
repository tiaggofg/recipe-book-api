package com.zord.recipe.api.exceptions;

public class ExistsUserIdException extends RuntimeException {

    public ExistsUserIdException(String msg) {
        super(msg);
    }

}
