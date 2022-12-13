package com.zord.recipe.api.services;

import com.zord.recipe.api.model.Comment;
import com.zord.recipe.api.repositories.CommentRepository;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment update(String id, Comment comment) {
        return commentRepository.update(id, comment);
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.create(comment);
    }

    @Override
    public void delete(String id) {
        commentRepository.delete(id);
    }

}
