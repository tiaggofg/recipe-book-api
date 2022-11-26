package com.zord.recipe.api.controllers;

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
		var id = ctx.pathParam("id");
		ctx.json(recipeService.findById(id));
	}
	
	@Override
	public void getByIngredient(Context ctx) {
		 var ingredient = ctx.queryParamMap().values().stream().findAny().get().get(0);
		 ctx.json(recipeService.findByIngredient(ingredient));
	}
	
	@Override
	public void getBySearch(Context ctx) {
		var search = ctx.queryParamMap().values().stream().findAny().get().get(0);
		ctx.json(recipeService.searchInTitleAndDescription(search));
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
		var id = ctx.pathParam("id");
		recipeService.update(id, recipe);
	}

	@Override
	public void delete(Context ctx) {
		var id = ctx.pathParam("id");
		recipeService.delete(id);
	}
	
	@Override
	public void postLike(Context ctx) {
		var userId = Integer.parseInt(ctx.pathParam("userId"));
		var recipeId = ctx.pathParam("id");
		ctx.json(recipeService.addLike(userId, recipeId));
	}
	
	@Override
	public void deleteLike(Context ctx) {
		var userId = Integer.parseInt(ctx.pathParam("userId"));
		var recipeId = ctx.pathParam("id");
		recipeService.removeLike(userId, recipeId);
	}

}