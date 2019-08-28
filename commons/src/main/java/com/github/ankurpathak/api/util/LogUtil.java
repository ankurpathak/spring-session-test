package com.github.ankurpathak.api.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class LogUtil {

    private LogUtil(){}

    public static void logInfo(Logger log, String message){
        log.info(message);
    }

    public static void logFieldNull(Logger log, String entity, String field, String id){
        log.error(MESSAGE_LOG_FIELD_NULL, entity, field, id);
    }

    public static void logValue(Logger log, String property, String value){
        log.error(MESSAGE_LOG_VALUE, property, value);
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

    public static void logStackTrace(Logger log, Throwable ex){
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(MESSAGE_EXCEPTION, stacktrace);
    }



    public static String MESSAGE_LOG_FIELD_NULL = "{}.{} is null for id {}";
    public static String MESSAGE_LOG_FIELD_EMPTY = "{}.{} is empty for id {}";


    public static String MESSAGE_LOG_NULL = "{}.{} is null";
    public static String MESSAGE_LOG_EMPTY = "{}.{} is empty";
    public static String MESSAGE_EXCEPTION = "Exception: {}";
    public static String MESSAGE_LOG_VALUE = "{} is {}";

}
