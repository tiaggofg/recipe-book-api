package com.recipe.book.api;

import com.google.inject.Guice;
import com.recipe.book.api.config.AppModule;
import com.recipe.book.api.config.Startup;

public class RecipeApplication {

    public static void main(String[] args) {
        Guice.createInjector(new AppModule())
                .getInstance(Startup.class)
                .boot();
    }
}
