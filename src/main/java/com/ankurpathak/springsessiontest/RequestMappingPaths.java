package com.ankurpathak.springsessiontest;

import java.nio.file.Paths;

public interface RequestMappingPaths {
    String PATH_API = "/api";
    String PATH_GET_ME = "/me";



    String PATH_GET_ERROR = "/error";


    String PATH_CREATE_USER = "/users";








    static String apiPath(String path){
        return Paths.get(PATH_API, path).toString();
    }

}
