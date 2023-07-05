package com.recipe.book.api.config;

import com.recipe.book.api.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static Properties applicationProperties;

    private ApplicationProperties() {
    }

    public static String getProperty(String key) {
        if (applicationProperties == null) {
            Log.warn("As propriedades da aplicação não foram iniciadas!", ApplicationProperties.class);
            Log.info("Carregando as propriedades da aplicação", ApplicationProperties.class);
            ApplicationProperties.load();
        }
        return applicationProperties.getProperty(key);
    }

    private static void load() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("recipe-book.properties");
        if (inputStream != null) {
            ApplicationProperties.loadFromInputStream(inputStream);
        } else {
            ApplicationProperties.loadFromEnvironmentVariables();
        }
        Log.info("Arquivo de configurações lido com sucesso!", ApplicationProperties.class);
    }

    private static void loadFromInputStream(InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            applicationProperties = properties;
        } catch (IOException e) {
            Log.error("Ocorreu um erro ao ler o arquivo de configuração!", ApplicationProperties.class, e);
            System.exit(0);
        }
    }

    private static void loadFromEnvironmentVariables() {
        try {
            //TODO:created a properties object and add the following attributes
        } catch (NumberFormatException e) {
            Log.error("Ocorreu um erro ao ler o arquivo de configuração!", ApplicationProperties.class, e);
            System.exit(0);
        }
    }
}
