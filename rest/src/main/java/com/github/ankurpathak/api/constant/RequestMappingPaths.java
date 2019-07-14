package com.github.ankurpathak.api.constant;

import java.nio.file.Paths;

public interface RequestMappingPaths {
    String PATH_API = "/api";
    String PATH_GET_ME = "/me";



    String PATH_GET_ERROR = "/error";


    String PATH_CREATE_USER = "/users";



    String PATH_ACCOUNT = "/account";


    String PATH_SEARCH_BY_FIEND_USER = "/users/search/{field}/{value}";


    String PATH_SEARCH_USER = "/users/search";



    String PATH_LIST_FIELD_USER = "/users/list/{field}/{value}";


    String PATH_ACCOUNT_EMAIL = "/account/email/{email}";

    String PATH_ACCOUNT_ENABLE = "/account/token/{token}";

    String PATH_FORGET_PASSWORD_EMAIL = "/password/email/{email}";


    String PATH_FORGET_PASSWORD = "/password";

    String PATH_FORGET_PASSWORD_ENABLE = "/password/token/{token}";
    String PATH_CHANGE_PASSWORD = "/password";
    String PATH_CHANGE_PROFILE = "/users";

    String PATH_LOGOUT = "/logout";

    String PATH_FAVICON = "/favicon.ico";
    String PATH_REMEMBER_ME = "/remember-me";


    static String apiPath(String path){
        return Paths.get(PATH_API, path).toString();
    }

}