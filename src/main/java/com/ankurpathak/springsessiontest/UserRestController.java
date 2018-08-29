package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigInteger;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_CREATE_USER;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_GET_ME;


@ApiController
public class UserRestController extends AbstractRestController<User,BigInteger,UserDto> {


    private final IUserService service;


    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public UserRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service) {
        super(applicationEventPublisher, messageSource);
        this.service = service;

    }

    @GetMapping(PATH_GET_ME)
    @JsonView(User.View.Me.class)
    public User get(@CurrentUser User user){
        return user;
    }



    @PostMapping(PATH_CREATE_USER)
    public ResponseEntity<?> createOne(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({DomainDto.Default.class}) UserDto dto, BindingResult result){
        return createOne(dto, result, request, response);
    }

}
