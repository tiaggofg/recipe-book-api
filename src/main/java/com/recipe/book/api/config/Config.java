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

    private int applicationPort;
    private int mongoPort;
    private String mongoHost;
    private String mongoDatabase;
    private String conectionString;

    public Config() {
    }

    public Config(Properties properties) {
        applicationPort = Integer.parseInt(properties.getProperty("app.port"));
        mongoPort = Integer.parseInt(properties.getProperty("db.port"));
        mongoHost = properties.getProperty("db.host");
        mongoDatabase = properties.getProperty("db.name");
        conectionString = properties.getProperty("db.stringConnection");
    }

    public int getApplicationPort() {
        return applicationPort;
    }

    public String getMongoDatabase() {
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

    public MongoClient getMongoAtlasClient() {
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

    public static Config fromEnvs() {
        Config config = new Config();
        config.applicationPort = Integer.parseInt(System.getenv("APPLICATION_PORT"));
        config.conectionString = System.getenv("STRING_CONNECTION");
        config.mongoDatabase = System.getenv("DATABASE");
        config.mongoPort = Integer.parseInt(System.getenv("DB_PORT"));
        config.mongoHost = System.getenv("DB_HOST");
        return config;
    }
}
