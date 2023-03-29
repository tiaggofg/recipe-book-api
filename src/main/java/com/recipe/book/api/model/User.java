package com.recipe.book.api.model;

import com.mongodb.DBRef;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private ObjectId id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private List<DBRef> recipes = new ArrayList<>();

    public User() {
    }

    public User(ObjectId id, String firstName, String lastName, String userName, String password, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<DBRef> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<DBRef> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName);
    }
}
