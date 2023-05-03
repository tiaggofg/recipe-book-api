package com.recipe.book.api.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Config extends Properties {

    private static int applicationPort;
    private static int mongoPort;
    private static String mongoHost;
    private static String mongoDatabase;
    private static String conectionString;

    public Config() {
    }

    public Config(Properties properties) {
        applicationPort = Integer.parseInt(properties.getProperty("app.port"));
        mongoPort = Integer.parseInt(properties.getProperty("db.port"));
        mongoHost = properties.getProperty("db.host");
        mongoDatabase = properties.getProperty("db.name");
        conectionString = properties.getProperty("db.stringConnection");
    }

    public static void setApplicationPort(int applicationPort) {
        Config.applicationPort = applicationPort;
    }

    public static int getMongoPort() {
        return mongoPort;
    }

    public static void setMongoPort(int mongoPort) {
        Config.mongoPort = mongoPort;
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public static void setMongoHost(String mongoHost) {
        Config.mongoHost = mongoHost;
    }

    public static void setMongoDatabase(String mongoDatabase) {
        Config.mongoDatabase = mongoDatabase;
    }

    public static String getConectionString() {
        return conectionString;
    }

    public static void setConectionString(String conectionString) {
        Config.conectionString = conectionString;
    }

    public int getApplicationPort() {
        return applicationPort;
    }

    public static String getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoClient getMongoClient() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(mongoHost, mongoPort))))
                .codecRegistry(pojoCodecRegistry).build());
    }

    public static MongoClient getMongoAtlasClient() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(conectionString))
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    public static void fromEnvs() {
        Config.applicationPort = Integer.parseInt(System.getenv("APPLICATION_PORT"));
        Config.conectionString = System.getenv("STRING_CONNECTION");
        Config.mongoDatabase = System.getenv("DATABASE");
        Config.mongoPort = Integer.parseInt(System.getenv("DB_PORT"));
        Config.mongoHost = System.getenv("DB_HOST");
    }
}
