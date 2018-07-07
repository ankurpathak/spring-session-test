package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_GET_ME;

@ApiController
public class MeController {


    @GetMapping(PATH_GET_ME)
    @JsonView(User.View.Public.class)
    public User me(@CurrentUser User user){
        return user;
    }
}
