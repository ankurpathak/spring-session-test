package com.ankurpathak.springsessiontest;

import org.springframework.http.ResponseEntity;

import java.nio.file.Paths;

public interface RequestMappingPaths {
    String PATH_API = "/api";
    String PATH_GET_ME = "/me";



    String PATH_GET_ERROR = "/error";


    String PATH_CREATE_USER = "/users";



    String PATH_REGISTER = "/register";


    static String apiPath(String path){
        return Paths.get(PATH_API, path).toString();
    }

}
