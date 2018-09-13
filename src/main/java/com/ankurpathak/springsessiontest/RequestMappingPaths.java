package com.ankurpathak.springsessiontest;

import java.nio.file.Paths;

public interface RequestMappingPaths {
    String PATH_API = "/api";
    String PATH_GET_ME = "/me";



    String PATH_GET_ERROR = "/error";


    String PATH_CREATE_USER = "/users";



    String PATH_REGISTER = "/register";


    String PATH_SEARCH_USER = "/users/search/{field}/{value}";


    String PATH_LIST_FIELD_USER = "/users/list/{field}/{value}";



    static String apiPath(String path){
        return Paths.get(PATH_API, path).toString();
    }

}
