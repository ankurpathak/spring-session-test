package com.github.ankurpathak.api.rest.controller;


import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.constant.ApiPaths;
import org.springframework.web.bind.annotation.GetMapping;

@ApiController
public class RememberMeController {

    @GetMapping(ApiPaths.PATH_REMEMBER_ME)
    public String ping(){
        return "remember-me";
    }
}
