package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.domain.converter.ProductConverters;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IProductService;
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

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_SERVICE;
import static com.github.ankurpathak.api.constant.ApiPaths.PATH_SERVICE_UPLOAD;

@ApiController
public class ProductController extends AbstractRestController<Product, String, ProductDto> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    private final IProductService service;

    public ProductController(IProductService service, ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
    }

    @Override
    public IDomainService<Product, String> getDomainService() {
        return service;
    }


    @PostMapping(PATH_SERVICE)
    public ResponseEntity<?> createOne(@CurrentBusiness Business business, HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({Default.class}) ProductDto dto, BindingResult result){
         return createOne(dto,result, request,response, ProductConverters.createOne);
    }



    @PostMapping(PATH_SERVICE_UPLOAD)
    public ResponseEntity<?> createMany(@CurrentBusiness Business business, HttpServletRequest request, HttpServletResponse response, @Validated(DomainDtoList.Upload.class) DomainDtoList<Product, String, ProductDto> csvList, BindingResult result){
        return createManyByCsv(csvList, ProductDto.class, Product.class, request, ProductConverters.createOne, log,result, Default.class);
    }



}