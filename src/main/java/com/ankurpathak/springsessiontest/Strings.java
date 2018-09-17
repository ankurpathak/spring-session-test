package com.ankurpathak.springsessiontest;

import org.springframework.util.StringUtils;

public class Strings {

    public static final String EMPTY = "";

    public static String makeFirstLowercase(String string){
        if(StringUtils.isEmpty(string))
            return string;
        else {
            String first = string.substring(0, 1);
            return first.toLowerCase() + string.substring(1);
        }
    }
}
