package com.recipe.book.api.services;

import com.recipe.book.api.model.User;
import io.javalin.security.BasicAuthCredentials;

public interface UserService {

    User findUser(String userId);

    User findByUsername(String userName);

    User updateUser(String userName, User user);

    void registerUser(User user);

    void deleteUser(String userName);

    boolean isValidCredentials(BasicAuthCredentials authCredentials);

    void addRecipe(String username, String id);

    void removeRecipe(String username, String recipeId);

    void addRecipeToListLike(User currentUser, String recipeId);

    void removeRecipeFromListLike(User currentUser, String recipeId);
}
