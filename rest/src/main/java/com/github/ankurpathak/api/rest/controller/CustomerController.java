package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.domain.converter.CustomerConverters;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.ICustomerService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

import static com.github.ankurpathak.api.constant.ApiPaths.*;

@ApiController
public class CustomerController extends AbstractRestController<Customer, CustomerId, CustomerDto> {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);


    private final ICustomerService service;

    public CustomerController(ICustomerService service, ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
    }

    @Override
    public IDomainService<Customer, CustomerId> getDomainService() {
        return service;
    }


    @PostMapping(PATH_CUSTOMER)
    public ResponseEntity<?> createOne(@CurrentBusiness Business business, HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({Default.class}) CustomerDto dto, BindingResult result){
        return createOne(dto,result, request,response, CustomerConverters.createOne, (rest, tDto) -> {
            User user = service.processUser(business, dto);
            tDto.businessId(business.getId());
            tDto.userId(user.getId());
        }, (rest, t, tDto) -> { });
    }




    @PostMapping(PATH_CUSTOMER_UPLOAD)
    public ResponseEntity<?> createMany(@CurrentBusiness Business business, HttpServletRequest request, HttpServletResponse response, @Validated(DomainDtoList.Upload.class) DomainDtoList<Customer, CustomerId, CustomerDto> csvList, BindingResult result){
        return createManyByCsv(csvList, CustomerDto.class, Customer.class, request, CustomerConverters.createOne, log,result, Default.class);
    }





}
