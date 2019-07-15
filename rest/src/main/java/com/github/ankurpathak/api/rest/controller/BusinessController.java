package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.domain.converter.BusinessConverters;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_BUSINESS;

@ApiController
public class BusinessController extends AbstractRestController<Business, BigInteger, BusinessDto> {

    private final IBusinessService service;
    private final CustomUserDetailsService userDetailsService;

    public BusinessController(IBusinessService service, ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, CustomUserDetailsService userDetailsService) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public IDomainService<Business, BigInteger> getDomainService() {
        return service;
    }


    @PostMapping(PATH_BUSINESS)
    public ResponseEntity<?> createOne(@CurrentUser User user, HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({DomainDto.Default.class}) BusinessDto dto, BindingResult result){
        return createOne(dto, result, request, response, BusinessConverters.businessDtoCreateOneDomain, (rest, tDto) -> {}, (rest,t ,tDto) -> {
            user.addBusinessId(t.getId());
            userDetailsService.getUserService().update(user);
        });
    }
}
