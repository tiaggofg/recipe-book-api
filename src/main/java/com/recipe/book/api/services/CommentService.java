package com.recipe.book.api.services;

import com.recipe.book.api.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(String id);

    Comment update(String id, Comment comment);

    Comment create(Comment comment);

    void delete(String id);

}
