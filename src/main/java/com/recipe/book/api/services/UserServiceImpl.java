package com.recipe.book.api.services;

import com.recipe.book.api.exceptions.InvalidCredentialsException;
import com.recipe.book.api.exceptions.UserLikedRecipeException;
import com.recipe.book.api.exceptions.UserNotLikeRecipeException;
import com.recipe.book.api.model.User;
import com.recipe.book.api.repositories.UserRepository;
import io.javalin.security.BasicAuthCredentials;

public class UserServiceImpl implements UserService {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findUser(String userId) {
        return repository.findUser(userId);
    }

    @Override
    public User findByUsername(String userName) {
        return repository.findByUsername(userName);
    }

    @Override
    public User updateUser(String userName, User user) {
        return repository.updateUser(userName, user);
    }

    @Override
    public void registerUser(User user) {
        repository.registerUser(user);
    }

    @Override
    public void deleteUser(String userName) {
        repository.deleteUser(userName);
    }

    @Override
    public boolean isValidCredentials(BasicAuthCredentials authCredentials) {
        if (authCredentials == null) {
            throw new InvalidCredentialsException("Usuário ou senha inválidos!");
        }
        String userName = authCredentials.getUsername();
        String password = authCredentials.getPassword();
        User user = repository.findByUsername(userName);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public void addRecipe(String username, String recipeId) {
        repository.addRecipe(username, recipeId);
    }

    @Override
    public void removeRecipe(String username, String recipeId) {
        repository.removeRecipe(username, recipeId);
    }

    @Override
    public void addRecipeToListLike(User currentUser, String recipeId) {
        if (currentUser.getLikedRecipesList().contains(recipeId)) {
            throw new UserLikedRecipeException("User id: " + currentUser.getId() + " already liked the recipe!");
        }
        repository.addRecipeToListLike(currentUser, recipeId);
    }

    @Override
    public void removeRecipeFromListLike(User currentUser, String recipeId) {
        if (!currentUser.getLikedRecipesList().contains(recipeId)) {
            throw new UserNotLikeRecipeException("User id: " + currentUser.getId() + " did not like the recipe!");
        }
        repository.removeRecipeFromListLike(currentUser, recipeId);
    }
}
