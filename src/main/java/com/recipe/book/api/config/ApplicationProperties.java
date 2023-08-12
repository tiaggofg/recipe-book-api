package com.recipe.book.api.config;

import com.recipe.book.api.log.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static Properties applicationProperties;

    private ApplicationProperties() {
    }

    public static String getProperty(String key) {
        if (applicationProperties == null) {
            Log.error("As propriedades da aplicação não foram iniciadas!", ApplicationProperties.class);
            ApplicationProperties.load();
        }
        return applicationProperties.getProperty(key);
    }

    protected static void load() {
        Log.info("Iniciando propriedades da aplicação...", ApplicationProperties.class);
        try {
            InputStream inputStream = new FileInputStream("/etc/secrets/recipe-book.properties");
            ApplicationProperties.loadFromInputStream(inputStream);
        } catch (FileNotFoundException e) {
            Log.error("Não foi possível ler o arquivo de propriedades!", ApplicationProperties.class, e);
            System.exit(1);
        }
        Log.info("Propriedades da aplicação iniciadas com sucesso!", ApplicationProperties.class);
    }

    private static void loadFromInputStream(InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            applicationProperties = properties;
        } catch (IOException e) {
            Log.error("Ocorreu um erro ao carregar as propriedades da aplicação!", ApplicationProperties.class, e);
            System.exit(1);
        }
    }
}
