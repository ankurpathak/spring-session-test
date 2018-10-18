package com.ankurpathak.springsessiontest.controller;


import com.ankurpathak.springsessiontest.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

import static com.ankurpathak.springsessiontest.Params.FIELD;
import static com.ankurpathak.springsessiontest.Params.RSQL;
import static com.ankurpathak.springsessiontest.Params.VALUE;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;


@ApiController
public class UserRestController extends AbstractRestController<User,BigInteger,UserDto> {


    private final IUserService service;
    private final ToDomainConverters converters;


    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public UserRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service, ToDomainConverters converters) {
        super(applicationEventPublisher, messageSource);
        this.service = service;
        this.converters = converters;
    }

    @GetMapping(PATH_GET_ME)
    @JsonView(View.Me.class)
    public User get(@CurrentUser User user){
        return user;
    }



    @PostMapping(PATH_CREATE_USER)
    public ResponseEntity<?> createOne(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({DomainDto.Default.class}) UserDto dto, BindingResult result){
        return createOne(dto, result, request, response, converters.userDtoCreateToDomain);
    }

    /*

    @GetMapping(PATH_SEARCH_BY_FIEND_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @PathVariable("field") String field, @PathVariable("value") String value, @RequestParam(name = "size", required = false) String size, @RequestParam(value = "page", required = false, defaultValue = "1") String page, @RequestParam(value = "sort", required = false) String sort){
        return searchByField(field, value, PrimitiveUtils.toInteger(page), PrimitiveUtils.toInteger(size), sort, User.class, response);
    }


    @GetMapping(PATH_LIST_FIELD_USER)
    public List<String> listFields(@PathVariable("field") String field, @PathVariable("value") String value, @RequestParam(name = "size", required = false) String size, @RequestParam(value = "page", required = false, defaultValue = "1") String page, @RequestParam(value = "sort", required = false) String sort){
        return listField(field, value, PrimitiveUtils.toInteger(page), PrimitiveUtils.toInteger(size), sort, User.class);
    }


    @GetMapping(PATH_SEARCH_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response,@RequestParam("rsql") String rsql, @RequestParam(name = "size", required = false) String size, @RequestParam(value = "page", required = false, defaultValue = "1") String page, @RequestParam(value = "sort", required = false) String sort){
        return search(rsql, PrimitiveUtils.toInteger(page), PrimitiveUtils.toInteger(size), sort, User.class, response);
    }
    */

    @GetMapping(PATH_SEARCH_BY_FIEND_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @PathVariable(FIELD) String field, @PathVariable(VALUE) String value, Pageable pageable){
        return searchByField(field, value, pageable, User.class, response);
    }


    @GetMapping(PATH_LIST_FIELD_USER)
    public List<String> listFields(@PathVariable(FIELD) String field, @PathVariable(VALUE) String value, Pageable pageable){
        return listField(field, value, pageable, User.class);
    }


    @GetMapping(PATH_SEARCH_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response,@RequestParam(RSQL) String rsql, Pageable pageable){
        return search(rsql, pageable, User.class, response);
    }







}
