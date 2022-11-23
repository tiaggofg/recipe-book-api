package com.zord.recipe.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

	private String id;
	private String title;
	private String description;
	
	private List<Integer> likes = new ArrayList<>();
	private List<String> ingredients = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	
	public Recipe(String title, String description, List<Integer> likes, List<String> ingredients) {
		this.title = title;
		this.description = description;
		this.likes = likes;
		this.ingredients = ingredients;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getLikes() {
		return likes;
	}

	public List<String> getIngredients() {
		return ingredients;
	}
	
	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Recipe other = (Recipe) obj;
		return Objects.equals(id, other.id);
	}
	
}
