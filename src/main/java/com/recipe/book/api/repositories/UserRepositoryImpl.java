package com.recipe.book.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.recipe.book.api.model.User;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class UserRepositoryImpl implements UserRepository {

    private final MongoCollection<User> coll;

    public UserRepositoryImpl(MongoDatabase database) {
        coll = database.getCollection("user", User.class);
    }

    @Override
    public User findUser(String userId) {
        return coll.find(Filters.eq("_id", userId)).first();
    }

    @Override
    public User updateUser(String userName, User user) {
        Bson filter = Filters.eq("username", userName);
        return coll.findOneAndReplace(filter, user);
    }

    @Override
    public void registerUser(User user) {
        String id = new ObjectId().toString();
        user.setId(id);
        coll.insertOne(user);
    }

    @Override
    public void deleteUser(String userName) {
        coll.deleteOne(Filters.eq("username", userName));
    }

    @Override
    public User findByUsername(String userName) {
        return coll.find(Filters.eq("username", userName), User.class).first();
    }

    @Override
    public void addRecipe(String username, String recipeId) {
        Bson filter = Filters.eq("username", username);
        Bson update = Updates.push("recipeIds", recipeId);
        coll.updateOne(filter, update);
    }

    @Override
    public void removeRecipe(String username, String recipeId) {
        Bson filter = Filters.eq("username", username);
        Bson update = Updates.pull("recipeIds", recipeId);
        coll.updateOne(filter, update);
    }

    @Override
    public void addRecipeToListLike(User currentUser, String recipeId) {
        Bson filter = Filters.eq("_id", currentUser.getId());
        Bson update = Updates.push("likedRecipes", recipeId);
        coll.findOneAndUpdate(filter, update);
    }

    @Override
    public void removeRecipeFromListLike(User currentUser, String recipeId) {
        Bson filter = Filters.eq("_id", currentUser.getId());
        Bson update = Updates.pull("likedRecipes", recipeId);
        coll.findOneAndUpdate(filter, update);
    }
}
