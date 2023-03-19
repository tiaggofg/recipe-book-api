package com.recipe.book.api.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    public static void trace(String message, Class<?> clazz) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.trace(message);
    }

    public static void debug(String message, Class<?> clazz) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.debug(message);
    }

    public static void info(String message, Class<?> clazz) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.info(message);
    }

    public static void warn(String message, Class<?> clazz) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.warn(message);
    }

    public static void error(String message, Class<?> clazz) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.error(message);
    }

    public static void error(String message, Class<?> clazz, Throwable throwable) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.error(message, throwable);
    }
}