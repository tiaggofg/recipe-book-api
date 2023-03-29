package com.recipe.book.api.services;

import com.recipe.book.api.model.User;
import io.javalin.security.BasicAuthCredentials;

public interface UserService {

    User findUser(String userId);

    User findUserByName(String userName);

    User updateUser(String userName, User user);
    void registerUser(User user);
    void deleteUser(String userName);
    boolean isValidCredentials(BasicAuthCredentials authCredentials);
    void addRecipe(String username, String id);
    void removeRecipe(String username, String recipeId);
}
