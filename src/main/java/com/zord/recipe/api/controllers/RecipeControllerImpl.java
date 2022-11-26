package com.zord.recipe.api.controllers;

import org.bson.types.ObjectId;

import com.zord.recipe.api.model.Recipe;
import com.zord.recipe.api.services.RecipeService;

import io.javalin.http.Context;

public class RecipeControllerImpl implements RecipeController {

	private RecipeService recipeService;
	
	public RecipeControllerImpl(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@Override
	public void get(Context ctx) {
		ctx.json(recipeService.findAll());
	}

	@Override
	public void getById(Context ctx) {
		var id = new ObjectId(ctx.pathParamAsClass("id", String.class).get());
		ctx.json(recipeService.findById(id));
	}

	@Override
	public void post(Context ctx) {
		var recipe = ctx.bodyAsClass(Recipe.class);
		var recipeCreated = recipeService.create(recipe);
		ctx.json(recipeCreated);
	}

	@Override
	public void put(Context ctx) {
		var recipe = ctx.bodyAsClass(Recipe.class);
		var id = new ObjectId(ctx.pathParamAsClass("id", String.class).get());
		recipeService.update(id, recipe);
	}

	@Override
	public void delete(Context ctx) {
		var id = new ObjectId(ctx.pathParamAsClass("id", String.class).get());
		recipeService.delete(id);
	}

}