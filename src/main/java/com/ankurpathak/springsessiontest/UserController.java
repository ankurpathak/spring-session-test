package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

import static com.ankurpathak.springsessiontest.PathVariables.ID;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_CREATE_USER;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_GET_USER;


@ApiController
public class UserController {

    private final MessageSource messageSource;

    public UserController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping(PATH_GET_USER)
    @JsonView(User.View.Public.class)
    public User get(@PathVariable(ID) String id){
        return ContextRefreshedListener.users.stream().filter(user -> Objects.equals(id, user.getId())).findFirst().orElseThrow(() -> new NotFoundException(id, PathVariables.ID, User.class.getSimpleName(), ApiCode.USER_NOT_FOUND));
    }



    @PostMapping(PATH_CREATE_USER)
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody @Valid UsernameDto dto, BindingResult result){
        ControllerUtil.processValidaton(result, messageSource, request);
        System.out.println(dto);
        return ControllerUtil.processSuccess(messageSource, request, HttpStatus.CREATED);
    }

}
