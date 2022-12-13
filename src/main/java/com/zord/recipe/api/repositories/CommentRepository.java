package com.zord.recipe.api.repositories;

import com.zord.recipe.api.model.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findAll();

    Comment findById(String id);

    Comment update(String id, Comment comment);

    Comment create(Comment comment);

    void delete(String id);

}
