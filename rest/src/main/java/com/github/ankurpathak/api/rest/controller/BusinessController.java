package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.domain.converter.BusinessConverters;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Contact;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.BusinessUpdaters;
import com.github.ankurpathak.api.event.BusinessAddedEvent;
import com.github.ankurpathak.api.event.EmailTokenEvent;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
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
    public ResponseEntity<?> createOne(@CurrentUser User user, HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated({Default.class, BusinessDto.Account.class}) BusinessDto dto, BindingResult result) {
        return createOne(dto, result, request, response, BusinessConverters.createOne,
                (rest, tDto) -> {
                    if(CollectionUtils.isNotEmpty(user.getBusinessIds()) && user.getBusinessIds().size() > 0)
                        ControllerUtil.processNotAllowed(messageService, ApiCode.MULTIPLE_BUSINESS_NOT_ALLOWED);
                },

                (rest, t, tDto) -> {
                    if (dto.getEmail() != null && (CollectionUtils.isEmpty(user.getBusinessIds()) && user.getEmail() == null)) {
                        user.email(Contact.getInstance(dto.getEmail()));
                        applicationEventPublisher.publishEvent(new EmailTokenEvent(user));
                    }
                    user.addBusinessId(t.getId());
                    User newUser = User.getInstance(user);
                    userDetailsService.getUserService().update(newUser);
                    if (user.getEmail() != null) {
                        applicationEventPublisher.publishEvent(new BusinessAddedEvent(t, user));
                    }
                });
    }


    @PutMapping(PATH_BUSINESS)
    public ResponseEntity<?> update(@CurrentBusiness Business business, HttpServletRequest request,  @RequestBody @Validated({Default.class}) BusinessDto dto, BindingResult result){
        return update(dto, business, BusinessUpdaters.updateBusiness, request, result);
    }


}
