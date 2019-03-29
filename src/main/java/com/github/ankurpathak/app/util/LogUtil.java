package com.github.ankurpathak.app.util;

import org.slf4j.Logger;

public class LogUtil {

    private LogUtil(){}

    public static void logFieldNull(Logger log, String entity, String field, String id){
        log.error(MESSAGE_LOG_FIELD_NULL, entity, field, id);
    }

    public static void logFieldEmpty(Logger log, String entity, String field, String id){
        log.error(MESSAGE_LOG_FIELD_EMPTY, entity, field, id);
    }

    public static void logNull(Logger log, String key){
        log.error(MESSAGE_LOG_NULL, key);
    }

    public static void logEmpty(Logger log, String key){
        log.error(MESSAGE_LOG_EMPTY, key);
    }



    public static String MESSAGE_LOG_FIELD_NULL = "{}.{} is null for id {}";
    public static String MESSAGE_LOG_FIELD_EMPTY = "{}.{} is empty for id {}";


public static String MESSAGE_LOG_NULL = "{}.{} is null";
    public static String MESSAGE_LOG_EMPTY = "{}.{} is empty";

}