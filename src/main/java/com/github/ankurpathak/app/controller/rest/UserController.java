package com.github.ankurpathak.app.controller.rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.*;
import com.github.ankurpathak.app.annotation.ApiController;
import com.github.ankurpathak.app.annotation.CurrentUser;
import com.github.ankurpathak.app.config.ControllerUtil;
import com.github.ankurpathak.app.controller.rest.dto.AbstractRestController;
import com.github.ankurpathak.app.controller.rest.dto.DomainDto;
import com.github.ankurpathak.app.controller.rest.dto.View;
import com.github.ankurpathak.app.controller.rest.dto.converter.DtoConverters;
import com.github.ankurpathak.app.domain.converter.DomainConverters;
import com.github.ankurpathak.app.domain.updater.DomainUpdaters;
import com.github.ankurpathak.app.service.IDomainService;
import com.github.ankurpathak.app.service.IMessageService;
import com.github.ankurpathak.app.service.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;


@ApiController
public class UserController extends AbstractRestController<User,BigInteger, UserDto> {


    private final IUserService service;
    private final DomainConverters converters;
    private final DomainUpdaters updaters;
    private final DtoConverters dtoConverters;


    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public UserController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IUserService service, DomainConverters converters, DomainUpdaters updaters, DtoConverters dtoConverters) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
        this.converters = converters;
        this.updaters = updaters;
        this.dtoConverters = dtoConverters;
    }

    @GetMapping(RequestMappingPaths.PATH_GET_ME)
    @JsonView(View.Me.class)
    public User get(@CurrentUser User user){
        return user;
    }



    @PostMapping(RequestMappingPaths.PATH_CREATE_USER)
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

    @GetMapping(RequestMappingPaths.PATH_SEARCH_BY_FIEND_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @PathVariable(Params.FIELD) String field, @PathVariable(Params.VALUE) String value, Pageable pageable){
        return searchByField(field, value, pageable, User.class, response);
    }


    @GetMapping(RequestMappingPaths.PATH_LIST_FIELD_USER)
    public List<String> listFields(@PathVariable(Params.FIELD) String field, @PathVariable(Params.VALUE) String value, Pageable pageable){
        return listField(field, value, pageable, User.class);
    }


    @GetMapping(RequestMappingPaths.PATH_SEARCH_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @RequestParam(Params.RSQL) String rsql, Pageable pageable){
        return search(rsql, pageable, User.class, response);
    }



    @PutMapping(RequestMappingPaths.PATH_CHANGE_PROFILE)
    public ResponseEntity<?> update(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({DomainDto.Default.class}) UserDto dto, BindingResult result){
        ControllerUtil.processValidation(result, messageService);
        return update(dto, user, updaters.profileUpdater, request);
    }


    @PatchMapping(RequestMappingPaths.PATH_CHANGE_PROFILE)
    public ResponseEntity<?> patch(HttpServletRequest request, @CurrentUser User user, @RequestBody JsonNode patch, BindingResult result){
        ControllerUtil.processValidation(result, messageService);
        return patch(patch, user, dtoConverters.userToUserDto, updaters.profileUpdater, UserDto.class, DomainDto.Default.class);
    }







}
