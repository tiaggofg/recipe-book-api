package com.recipe.book.api;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.recipe.book.api.config.Config;
import com.recipe.book.api.controllers.RecipeController;
import com.recipe.book.api.controllers.RecipeControllerImpl;
import com.recipe.book.api.controllers.UserController;
import com.recipe.book.api.controllers.UserControllerImpl;
import com.recipe.book.api.exceptions.*;
import com.recipe.book.api.log.Log;
import com.recipe.book.api.repositories.CommentRepositoryImpl;
import com.recipe.book.api.repositories.RecipeRepositoryImpl;
import com.recipe.book.api.repositories.UserRepository;
import com.recipe.book.api.repositories.UserRepositoryImpl;
import com.recipe.book.api.services.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RecipeApplication {

    public static void main(String[] args) {
        String pwd = new File("").getAbsolutePath();

        Config appConfig = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(pwd + "/recipe-book.properties");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            appConfig = new Config(properties);
        } catch (IOException e) {
            Log.error("Ocorreu um erro ao ler o arquivo de configuração!", RecipeApplication.class, e);
            System.exit(0);
        }

        MongoClient mongoClient = appConfig.getMongoAtlasClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(appConfig.getMongoDatabase());

        CommentService commentService = new CommentServiceImpl(new CommentRepositoryImpl(mongoDatabase));
        RecipeService recipeService = new RecipeServiceImpl(new RecipeRepositoryImpl(mongoDatabase));
        RecipeController recipeController = new RecipeControllerImpl(recipeService, commentService);

        UserRepository userRepository = new UserRepositoryImpl(mongoDatabase);
        UserService userService = new UserServiceImpl(userRepository);
        UserController userController = new UserControllerImpl(userService);

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "JSON";
            config.accessManager(userController::authenticate);
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(appConfig.getApplicationPort());

        app.routes(() -> {
            path("/", () -> {
                get(RecipeApplication::forbidden);
            });
            path("/user", () -> {
               post(userController::postUser);
            });
            path("recipe", () -> {
                get(recipeController::get);
                post(recipeController::post);
                path("ingredient", () -> {
                    get(recipeController::getByIngredient);
                });
                path("search", () -> {
                    get(recipeController::getBySearch);
                });
                path("{id}", () -> {
                    get(recipeController::getById);
                    put(recipeController::put);
                    delete(recipeController::delete);
                    path("like", () -> {
                        path("{userId}", () -> {
                            post(recipeController::postLike);
                            delete(recipeController::deleteLike);
                        });
                    });
                    path("comment", () -> {
                        post(recipeController::postComment);
                        path("{commentId}", () -> {
                            put(recipeController::putComment);
                            delete(recipeController::deleteComment);
                        });
                    });
                });
            });
        });

        app.exception(InvalidCredentialsException.class, (e, ctx) -> {
            Log.error(e.getMessage(), RecipeApplication.class);
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(MongoSocketOpenException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String errorMessage = "Ocorreu um erro no servidor. Entre em contato com o administrador!";
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(MismatchedInputException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String errorMessage = "Nenhum conteúdo enviado no request body!";
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
        });

        app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
           HttpStatus status = HttpStatus.BAD_REQUEST;
           String errorMessage = "Request body inválido. Envie um JSON conforme específicado na documentação!";
           DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), errorMessage, ctx.path());
           ctx.json(error).status(status);
        });

        app.exception(ObjectNotFoundException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.NOT_FOUND;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(ExistsUserIdException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.CONFLICT;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });

        app.exception(IdInvalidException.class, (e, ctx) -> {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DefaultError error = new DefaultError(String.valueOf(System.currentTimeMillis()), status.toString(), e.getMessage(), ctx.path());
            ctx.json(error).status(status);
        });
    }

    public static void forbidden(Context ctx) {
        ctx.status(HttpStatus.FORBIDDEN);
    }
}
