package com.recipe.book.api.repositories;

import com.recipe.book.api.model.User;

public interface UserRepository {

    User findUser(String userId);

    User updateUser(String userName, User user);

    void registerUser(User user);

    void deleteUser(String userName);

    User findByUsername(String userName);

    void addRecipe(String username, String recipeId);

    void removeRecipe(String username, String recipeId);

    void addRecipeToListLike(User currentUser, String recipeId);

    void removeRecipeFromListLike(User currentUser, String recipeId);
}
