package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_REGISTER;

@ApiController
public class RegisterRestController extends AbstractRestController<User, BigInteger, UserDto> {
    private final IUserService service;

    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public RegisterRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service) {
        super(applicationEventPublisher, messageSource);
        this.service = service;
    }


    @PostMapping(PATH_REGISTER)
    public ResponseEntity<?> register(HttpServletRequest request, HttpServletResponse response, @Validated({UserDto.Default.class, UserDto.Register.class}) @RequestBody UserDto dto, BindingResult result){
        try{
            User user = tryCreateOne(dto, result, request, response, UserDto.Register.class);
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user));
            return ControllerUtil.processSuccessCreated(messageSource, request);
        }catch (DuplicateKeyException ex){
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }








}
