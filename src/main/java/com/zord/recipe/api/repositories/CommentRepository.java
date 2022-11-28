package com.zord.recipe.api.repositories;

import java.util.List;

import com.zord.recipe.api.model.Comment;

public interface CommentRepository {

	List<Comment> findAll();
	Comment findById(String id);
	Comment update(String id, Comment comment);
	Comment create(Comment comment);
	void delete(String id);
	
}
