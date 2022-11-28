package com.zord.recipe.api.services;

import java.util.List;

import com.zord.recipe.api.model.Comment;

public interface CommentService {

	List<Comment> findAll();
	Comment findById(String id);
	Comment update(String id, Comment comment);
	Comment create(Comment comment);
	void delete(String id);

}
