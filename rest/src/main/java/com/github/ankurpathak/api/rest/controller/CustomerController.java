package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.CustomerConverters;
import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.ICustomerService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.impl.UserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return createManyByCsv(csvList, CustomerDto.class, Customer.class, request, CustomerConverters.createOne, log,result,(rest, list) -> {
            Map<String, CustomerDto> dtoListMap = list.getDtos().stream().collect(Collectors.toMap(CustomerDto::getPhone, Function.identity()));
            service.processUsers(business, dtoListMap)
                    .forEach(user -> {
                        Optional.ofNullable(user)
                                .map(User::getPhone)
                                .map(Contact::getValue)
                                .ifPresentOrElse(phone -> {
                                    dtoListMap.get(phone).userId(user.getId());
                                    dtoListMap.get(phone).businessId(business.getId());
                                }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), "contact.value", String.valueOf(user.getId())));
                    });
        }, (rest, list, count) ->{}, Default.class);
    }


    @GetMapping(PATH_CUSTOMER)
    public ResponseEntity<?> paginated(@CurrentBusiness Business business, HttpServletResponse response, Pageable pageable){
        return paginated(pageable, response, (Class)VCustomerUser.class, Model.VCustomerUser.V_CUSTOMER_USER);
    }

    @GetMapping(PATH_CUSTOMER_SEARCH)
    public ResponseEntity<?> search(@CurrentBusiness Business business, HttpServletResponse response, @RequestParam(Params.Query.RSQL) String rsql, Pageable pageable){
        return search(rsql, pageable, Customer.class, response);
    }





}
