package com.recipe.book.api.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Like {

    private String authorId;

    @BsonProperty(value = "username")
    private String userName;

    public Like() {
    }

    public Like(String userName, String authorId) {
        this.authorId = authorId;
        this.userName = userName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
