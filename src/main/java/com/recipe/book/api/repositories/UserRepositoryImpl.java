package com.recipe.book.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.recipe.book.api.model.User;
import org.bson.conversions.Bson;

public class UserRepositoryImpl implements UserRepository {

    private MongoCollection<User> coll;

    public UserRepositoryImpl(MongoDatabase database) {
        coll = database.getCollection("user", User.class);
    }

    @Override
    public User findUser(String userId) {
        return coll.find(Filters.eq("_id", userId)).first();
    }

    @Override
    public User updateUser(String userName, User user) {
        Bson filter = Filters.eq("userName", userName);
        return coll.findOneAndReplace(filter, user);
    }

    @Override
    public void registerUser(User user) {
        coll.insertOne(user);
    }

    @Override
    public void deleteUser(String userName) {
        coll.deleteOne(Filters.eq("userName", userName));
    }

    @Override
    public User findUserByName(String userName) {
        return coll.find(Filters.eq("userName", userName), User.class).first();
    }
}
