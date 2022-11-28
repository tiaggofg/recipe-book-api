package com.zord.recipe.api.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zord.recipe.api.model.Comment;

public class CommentRepositoryImpl implements CommentRepository {

	private MongoCollection<Comment> coll;

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
		return coll.find(filter).first();
	}

	@Override
	public Comment update(String id, Comment comment) {
		Bson filter = Filters.eq("_id", id);
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
		coll.deleteOne(filter);
	}

}
