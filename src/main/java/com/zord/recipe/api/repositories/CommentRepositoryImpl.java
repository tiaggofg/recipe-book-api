package com.zord.recipe.api.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zord.recipe.api.exceptions.ObjectNotFoundException;
import com.zord.recipe.api.model.Comment;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    private final MongoCollection<Comment> coll;

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
        if (coll.find(filter).first() == null) {
            throw new ObjectNotFoundException("Comentário id: " + id + " não encontrado!");
        }
        return coll.findOneAndReplace(filter, comment);
    }

    @Override
    public Comment create(Comment comment) {
        var id = new ObjectId().toString();
        comment.setId(id);
        coll.insertOne(comment);
        return findById(id);
    }

    @Override
    public void delete(String id) {
        Bson filter = Filters.eq("_id", id);
        if (coll.find(filter).first() == null) {
            throw new ObjectNotFoundException("Comentário id: " + id + " não encontrado!");
        }
        coll.deleteOne(filter);
    }
}
