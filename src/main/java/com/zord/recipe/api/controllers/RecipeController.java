package com.zord.recipe.api.controllers;

import io.javalin.http.Context;

public interface RecipeController {

	void get(Context ctx);
	void getById(Context ctx);
	void post(Context ctx);
	void put(Context ctx);
	void delete(Context ctx);
	
}
