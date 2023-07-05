package com.recipe.book.api.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {

    private static final String MONGO_HOST = "db.host";
    private static final String MONGO_PORT = "db.port";
    private static final String CONNECTION_STRING = "db.stringConnection";
    private static final String DATABASE_NAME = "db.name";

    public static MongoClient getMongoClient() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> {
                    String host = ApplicationProperties.getProperty(MONGO_HOST);
                    int port = Integer.parseInt(ApplicationProperties.getProperty(MONGO_PORT));
                    builder.hosts(List.of(
                            new ServerAddress(host, port))
                    );
                })
                .codecRegistry(pojoCodecRegistry)
                .build());
    }

    public static MongoClient getMongoAtlasClient() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(ApplicationProperties.getProperty(CONNECTION_STRING)))
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    public static MongoDatabase getDatabase() {
        return getMongoAtlasClient().getDatabase(ApplicationProperties.getProperty(DATABASE_NAME));
    }
}
