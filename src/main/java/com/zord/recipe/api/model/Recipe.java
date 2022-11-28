package com.zord.recipe.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Recipe implements Comparable<Recipe> {

	@JsonSerialize(using = ToStringSerializer.class)
	
	private String id;
	private String title;
	private String description;
	private List<String> ingredients = new ArrayList<>();
	private List<Integer> likes = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	
	public Recipe() {
	}
	
	public Recipe(String title, String description, List<String> ingredients) {
		this.title = title;
		this.description = description;
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
	
	public void setLikes(List<Integer> likes) {
		this.likes = likes;
	}

	public List<String> getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
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

	@Override
	public int compareTo(Recipe other) {
		return getTitle().compareTo(other.getTitle());
	}

}
