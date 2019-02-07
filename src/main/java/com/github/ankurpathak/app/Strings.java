package com.github.ankurpathak.app;

import org.springframework.util.StringUtils;

public class Strings {

    public static final String EMPTY = "";
    public static final String CARET = "^";
    public static final String DOLLAR = "$";
    public static final String COMMA = ",";
    public static final String ASTERISK = "*";

    public static String makeFirstLowercase(String string){
        if(StringUtils.isEmpty(string))
            return string;
        else {
            String first = string.substring(0, 1);
            return first.toLowerCase() + string.substring(1);
        }
    }
}
