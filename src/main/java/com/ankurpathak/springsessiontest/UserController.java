package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

import static com.ankurpathak.springsessiontest.PathVariables.ID;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_GET_USER;


@ApiController
public class UserController {


    @GetMapping(PATH_GET_USER)
    @JsonView(User.View.Public.class)
    public User get(@PathVariable(ID) String id){
        return CustomUserDetailsService.users.stream().filter(user -> Objects.equals(id, user.getId())).findFirst().orElseThrow(() -> new NotFoundException(id, PathVariables.ID, User.class.getSimpleName(), ApiCode.USER_NOT_FOUND));
    }

}
