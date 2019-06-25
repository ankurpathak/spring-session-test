package com.github.ankurpathak.app.util;

import com.github.ankurpathak.app.Strings;
import org.springframework.core.env.Environment;

public class PropertyUtil {



    public static String getProperty(Environment environment, String key){
        return getProperty(environment, key, Strings.EMPTY);
    }

    public static String getProperty(Environment environment, String key, String defaultValue){
        return environment.getProperty(key, defaultValue);
    }


    public static <T> T getProperty(Environment environment, String key, Class<T> type, T defaultValue){
        return environment.getProperty(key, type, defaultValue);
    }

}
