package com.recipe.book.api.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.mongodb.client.MongoDatabase;
import com.recipe.book.api.controllers.*;
import com.recipe.book.api.repositories.*;
import com.recipe.book.api.services.*;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import javax.inject.Singleton;

public class AppModule extends AbstractModule {

    @Override
    public void configure() {
        Multibinder<Routing> binder = Multibinder.newSetBinder(binder(), Routing.class);
        binder.addBinding().to(UserRouting.class);
        binder.addBinding().to(RecipeRouting.class);

        bind(UserController.class).to(UserControllerImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);

        bind(RecipeController.class).to(RecipeControllerImpl.class);
        bind(RecipeService.class).to(RecipeServiceImpl.class);
        bind(RecipeRepository.class).to(RecipeRepositoryImpl.class);

        bind(CommentService.class).to(CommentServiceImpl.class);
        bind(CommentRepository.class).to(CommentRepositoryImpl.class);

        bind(RecipeBookApi.class);
    }

    @Provides
    @Singleton
    public static Javalin provideJavalin() {
        int port = Integer.parseInt(ApplicationProperties.getProperty("app.port"));
        return Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(port);
    }

    @Provides
    @Singleton
    protected static MongoDatabase provideDatabase() {
        return MongoDB.getDatabase();
    }
}
