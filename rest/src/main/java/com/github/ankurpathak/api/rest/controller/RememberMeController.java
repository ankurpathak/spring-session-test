package com.github.ankurpathak.api.rest.controller;


import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.constant.RequestMappingPaths;
import org.springframework.web.bind.annotation.GetMapping;

import javax.ws.rs.PathParam;

@ApiController
public class RememberMeController {

    @GetMapping(RequestMappingPaths.PATH_REMEMBER_ME)
    public String ping(){
        return "remember-me";
    }
}
