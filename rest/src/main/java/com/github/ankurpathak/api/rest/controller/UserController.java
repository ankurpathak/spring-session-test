package com.github.ankurpathak.api.rest.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.domain.converter.UserConverters;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.UserUpdaters;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.rest.controllor.dto.converter.UserDtoConverters;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IRestControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.math.BigInteger;


@ApiController
public class UserController extends AbstractRestController<User,BigInteger, UserDto> {
    private final CustomUserDetailsService service;
    public UserController(CustomUserDetailsService service, IRestControllerService restControllerService) {
        super(restControllerService);
        this.service = service;
    }
    @Override
    public IDomainService<User, BigInteger> getDomainService() {
        return service.getUserService();
    }

    @PostMapping(ApiPaths.PATH_USER)
    public ResponseEntity<?> createOne(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({Default.class}) UserDto dto, BindingResult result){
        return createOne(dto, result, request, response, UserConverters.createOne);
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

    /*
    @GetMapping(ApiPaths.PATH_SEARCH_BY_FIEND_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @PathVariable(Params.FIELD) String field, @PathVariable(Params.VALUE) String value, Pageable pageable){
        return searchByField(field, value, pageable, User.class, response);
    }


    @GetMapping(ApiPaths.PATH_LIST_FIELD_USER)
    public List<String> listFields(@PathVariable(Params.FIELD) String field, @PathVariable(Params.VALUE) String value, Pageable pageable){
        return listField(field, value, pageable, User.class);
    }


    @GetMapping(ApiPaths.PATH_SEARCH_USER)
    @JsonView(View.Public.class)
    public List<User> search(HttpServletResponse response, @RequestParam(Params.RSQL) String rsql, Pageable pageable){
        return search(rsql, pageable, User.class, response);
    }

     */

    @PutMapping(ApiPaths.PATH_USER)
    public ResponseEntity<?> update(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({Default.class}) UserDto dto, BindingResult result){
        return update(dto, user, UserUpdaters.profileUpdater, request, result);
    }


    @PatchMapping(ApiPaths.PATH_USER)
    public ResponseEntity<?> patch(HttpServletRequest request, @CurrentUser User user, @RequestBody JsonNode patch, BindingResult result){
        restControllerService.getRestControllerResponseService().processValidation(result);
        return patch(patch, user, UserDtoConverters.userToUserDto, UserUpdaters.profileUpdater, UserDto.class, Default.class);
    }

}
