package com.recipe.book.api.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.client.MongoDatabase;
import com.recipe.book.api.controllers.RecipeController;
import com.recipe.book.api.controllers.RecipeControllerImpl;
import com.recipe.book.api.controllers.UserController;
import com.recipe.book.api.controllers.UserControllerImpl;
import com.recipe.book.api.repositories.*;
import com.recipe.book.api.services.*;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class AppModule extends AbstractModule {

    @Override
    public void configure() {
        Config.load();
        bind(MongoDatabase.class).toInstance(Config.getMongoAtlasClient().getDatabase(Config.getMongoDatabase()));

//        install(new ControllerModel());
//        install(new ServiceModule());
//        install(new RepositryModule());

        bind(UserController.class).to(UserControllerImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);

        bind(RecipeController.class).to(RecipeControllerImpl.class);
        bind(RecipeService.class).to(RecipeServiceImpl.class);
        bind(RecipeRepository.class).to(RecipeRepositoryImpl.class);

        bind(CommentService.class).to(CommentServiceImpl.class);
        bind(CommentRepository.class).to(CommentRepositoryImpl.class);

        bind(Startup.class);
    }

    @Provides
    public static Javalin provideJavalin() {
        return Javalin.create(config -> {
            config.http.defaultContentType = "JSON";
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(Config.getApplicationPort());
    }
}
