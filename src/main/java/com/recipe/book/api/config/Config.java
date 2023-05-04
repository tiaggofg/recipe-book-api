package com.recipe.book.api.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.recipe.book.api.log.Log;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.io.InputStream;
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

    private Config() {
    }

    public static int getMongoPort() {
        return mongoPort;
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public static void setMongoDatabase(String mongoDatabase) {
        Config.mongoDatabase = mongoDatabase;
    }

    public static String getConectionString() {
        return conectionString;
    }

    public static int getApplicationPort() {
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

    public static void load() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("recipe-book.properties");
        if (inputStream != null) {
            Config.loadFromInputStream(inputStream);
        } else {
            Config.loadFromEnvironmentVariables();
        }
        Log.info("Arquivo de configurações lido com sucesso!", Config.class);
    }

    private static void loadFromInputStream(InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            applicationPort = Integer.parseInt(properties.getProperty("app.port"));
            mongoPort = Integer.parseInt(properties.getProperty("db.port"));
            mongoHost = properties.getProperty("db.host");
            mongoDatabase = properties.getProperty("db.name");
            conectionString = properties.getProperty("db.stringConnection");
        } catch (IOException e) {
            Log.error("Ocorreu um erro ao ler o arquivo de configuração!", Config.class, e);
            System.exit(0);
        }
    }

    private static void loadFromEnvironmentVariables() {
        try {
            Config.applicationPort = Integer.parseInt(System.getenv("APPLICATION_PORT"));
            Config.conectionString = System.getenv("STRING_CONNECTION");
            Config.mongoDatabase = System.getenv("DATABASE");
            Config.mongoPort = Integer.parseInt(System.getenv("DB_PORT"));
            Config.mongoHost = System.getenv("DB_HOST");
        } catch (NumberFormatException e) {
            Log.error("Ocorreu um erro ao ler o arquivo de configuração!", Config.class, e);
            System.exit(0);
        }
    }
}
