package com.recipe.book.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.recipe.book.api.exceptions.ObjectNotFoundException;
import com.recipe.book.api.model.Comment;
import org.bson.conversions.Bson;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    private final MongoCollection<Comment> coll;

    @Inject
    public CommentRepositoryImpl(MongoDatabase database) {
        coll = database.getCollection("comment", Comment.class);
    }

    @Override
    public List<Comment> findAll() {
        List<Comment> result = new ArrayList<>();
        for (Comment comment : coll.find()) {
            result.add(comment);
        }
        return result;
    }

    @Override
    public Comment findById(String id) {
        Bson filter = Filters.eq("_id", id);
        Comment comment = coll.find(filter).first();
        if (comment == null) {
            throw new ObjectNotFoundException("Comentário id: " + id + " não encontrado!");
        }
        return comment;
    }

    @Override
    public Comment update(String id, Comment comment) {
        Bson filter = Filters.eq("_id", id);
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        Comment updatedComment = coll.findOneAndReplace(filter, comment, options);
        if (updatedComment == null) {
            throw new ObjectNotFoundException("Comentário id: " + id + " não encontrado!");
        }
        return updatedComment;
    }

    @Override
    public Comment create(Comment comment) {
        //String id = new ObjectId().toString();
        //comment.setId(id);
        coll.insertOne(comment);
        return findById(comment.getId());
    }

    @Override
    public void delete(String id) {
        Bson filter = Filters.eq("_id", id);
        Comment commentDeleted = coll.findOneAndDelete(filter);
        if (commentDeleted == null) {
            throw new ObjectNotFoundException("Comentário id: " + id + " não encontrado!");
        }
    }
}
