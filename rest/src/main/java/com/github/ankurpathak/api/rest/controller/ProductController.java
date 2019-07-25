package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.domain.converter.BusinessConverters;
import com.github.ankurpathak.api.domain.converter.ProductConverters;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Contact;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.BusinessAddedEvent;
import com.github.ankurpathak.api.event.EmailTokenEvent;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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
import static com.github.ankurpathak.api.constant.ApiPaths.PATH_SERVICE;

@ApiController
public class ProductController extends AbstractRestController<Product, String, ProductDto> {

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
    public ResponseEntity<?> createOne(@CurrentBusiness Business business, HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({ProductDto.Default.class}) ProductDto dto, BindingResult result){
         return createOne(dto,result, request,response, ProductConverters.createOne);
    }
}
