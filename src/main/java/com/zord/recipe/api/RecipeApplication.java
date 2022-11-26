package com.zord.recipe.api;

import com.mongodb.client.MongoClient;
import com.zord.recipe.api.config.Config;
import com.zord.recipe.api.controllers.RecipeController;
import com.zord.recipe.api.controllers.RecipeControllerImpl;
import com.zord.recipe.api.repositories.RecipeRepositoryImpl;
import com.zord.recipe.api.services.RecipeServiceImpl;

import io.javalin.Javalin;

public class RecipeApplication {

	public static void main(String[] args) {
		Config config = new Config();
		MongoClient mongo = config.getMongoClient();
		RecipeController recipeController = new RecipeControllerImpl(
				new RecipeServiceImpl(new RecipeRepositoryImpl(mongo.getDatabase(config.getDbName()))));

		Javalin app = Javalin.create().start(config.getApplicationPort());

		app.get("/recipe", recipeController::get);
		app.get("/recipe/{id}", recipeController::getById);
		app.post("/recipe", recipeController::post);
		app.put("recipe/{id}", recipeController::put);
		app.delete("/recipe/{id}", recipeController::delete);
		
	}

}
