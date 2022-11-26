package com.zord.recipe.api;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

import com.mongodb.client.MongoClient;
import com.zord.recipe.api.config.Config;
import com.zord.recipe.api.controllers.RecipeController;
import com.zord.recipe.api.controllers.RecipeControllerImpl;
import com.zord.recipe.api.repositories.RecipeRepositoryImpl;
import com.zord.recipe.api.services.RecipeServiceImpl;

import io.javalin.Javalin;

public class RecipeApplication {

	public static void main(String[] args) {
		MongoClient mongoClient = Config.getMongoClient();
		RecipeController recipeController = new RecipeControllerImpl(
				new RecipeServiceImpl(new RecipeRepositoryImpl(mongoClient.getDatabase(Config.getCollection()))));

		Javalin app = Javalin.create().start(Config.getApplicationPort());

		app.routes(() -> {
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
				});
			});
		});
	}
}
