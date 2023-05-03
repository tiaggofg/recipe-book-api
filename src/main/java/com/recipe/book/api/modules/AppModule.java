package com.recipe.book.api.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.recipe.book.api.config.Config;
import com.recipe.book.api.controllers.RecipeController;
import com.recipe.book.api.controllers.RecipeControllerImpl;
import com.recipe.book.api.controllers.UserController;
import com.recipe.book.api.controllers.UserControllerImpl;
import com.recipe.book.api.repositories.*;
import com.recipe.book.api.services.*;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserController.class).to(UserControllerImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);

        bind(RecipeRepository.class).to(RecipeRepositoryImpl.class);
        bind(RecipeService.class).to(RecipeServiceImpl.class);
        bind(RecipeController.class).to(RecipeControllerImpl.class);

        bind(CommentService.class).to(CommentServiceImpl.class);
        bind(CommentRepository.class).to(CommentRepositoryImpl.class);
    }

    @Provides
    static MongoDatabase provideDatabase() {
        MongoClient mongoClient = Config.getMongoAtlasClient();
        return mongoClient.getDatabase(Config.getMongoDatabase());
    }
}
