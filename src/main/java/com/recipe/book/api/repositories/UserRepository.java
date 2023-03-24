package com.recipe.book.api.repositories;

import com.recipe.book.api.model.User;

public interface UserRepository {

    User findUser(String userId);
    User updateUser(String userName, User user);
    void registerUser(User user);
    void deleteUser(String userName);
    User findUserByName(String userName);
}
