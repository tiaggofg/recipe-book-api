package com.zord.recipe.api.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Config {

    private static final int applicationPort = 8080;
    private static final int mongoPort = 27017;
    private static final String mongoHost = "localhost";
    private static final String mongoDatabase = "zord_recipe";
    private static final String collection = "recipe";

    public Config() {
    }

    public static int getApplicationPort() {
        return applicationPort;
    }

    public static String getCollection() {
        return collection;
    }

    public static String getMongoDatabase() {
        return mongoDatabase;
    }

    public static MongoClient getMongoClient() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(mongoHost, mongoPort))))
                .codecRegistry(pojoCodecRegistry).build());
    }

}
