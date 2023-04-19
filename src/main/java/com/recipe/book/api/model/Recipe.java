package com.recipe.book.api.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@EqualsAndHashCode
public class Recipe implements Comparable<Recipe> {

    private String id;
    private String authorId;
    private String title;
    private String description;
    private List<String> ingredients = new ArrayList<>();
    private List<String> preparation = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    @Override
    public int compareTo(Recipe other) {
        return getTitle().compareTo(other.getTitle());
    }
}
