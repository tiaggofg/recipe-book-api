package com.recipe.book.api.config;

import com.google.inject.Guice;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
@SuppressWarnings("rawtypes")
public class RecipeBookApi {

    private final Set<Routing> routes;

    @Inject
    public RecipeBookApi(Set<Routing> routes) {
        this.routes = routes;
    }

    private void boot() {
        routes.forEach(Routing::bindRoutes);
    }

    public static void start() {
        Guice.createInjector(new AppModule())
                .getInstance(RecipeBookApi.class)
                .boot();
    }
}
