package com.zord.recipe.api.config;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.List;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Config {

	private int applicationPort = 8080;
	private int mongoPort = 27017;
	private String mongoHost = "localhost";
	private String dbName = "recipe";

	public Config() {
	}

	public int getApplicationPort() {
		return applicationPort;
	}

	public String getDbName() {
		return dbName;
	}

	public MongoClient getMongoClient() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(pojoCodecProvider));

		return MongoClients.create(MongoClientSettings.builder()
				.applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(mongoHost, mongoPort))))
				.codecRegistry(pojoCodecRegistry).build());
	}
	
}
