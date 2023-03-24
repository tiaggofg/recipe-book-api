package com.recipe.book.api.services;

import com.recipe.book.api.exceptions.InvalidCredentialsException;
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
    public User findUserByName(String userName) {
        return repository.findUserByName(userName);
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
            throw new InvalidCredentialsException("Credenciais inválidas!");
        }
        String userName = authCredentials.getUsername();
        String password = authCredentials.getPassword();
        User user = repository.findUserByName(userName);
        return user != null && user.getPassword().equals(password);
    }
}