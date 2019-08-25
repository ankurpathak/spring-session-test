package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.event.PaginatedResultsRetrievedEvent;
import com.github.ankurpathak.api.service.ICityService;
import com.github.ankurpathak.api.service.IRestControllerResponseService;
import com.github.ankurpathak.api.service.impl.util.PagingUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.List;

@ApiController
public class CityController {

    private final ICityService service;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IRestControllerResponseService restControllerResponseService;

    public CityController(ICityService service, ApplicationEventPublisher applicationEventPublisher, IRestControllerResponseService restControllerResponseService) {
        this.service = service;
        this.applicationEventPublisher = applicationEventPublisher;
        this.restControllerResponseService = restControllerResponseService;
    }


    @GetMapping(ApiPaths.PATH_STATE)
    public List<String> getStates(){
        return service.findStates();
    }


    @GetMapping(ApiPaths.PATH_PIN_CODE)
    public ResponseEntity<?> getPinCode(@PathVariable(Params.Path.PIN_CODE) String pincode){
        return restControllerResponseService.processObject(service.findPinCode(pincode).orElse(null), null, "City", pincode);
    }

    @GetMapping(ApiPaths.PATH_STATE_DISTRICT)
    public List<String> getDistricts(@QueryParam(Params.Query.STATE) String state){
        return service.findDistricts(state);
    }

    @GetMapping(ApiPaths.PATH_STATE_PIN_CODE)
    public List<String> findPinCodes(HttpServletResponse response,  @QueryParam(Params.Query.STATE) String state, @QueryParam(Params.Query.DISTRICT) String district, Pageable pageable){
        PagingUtil.pagePreCheck(pageable.getPageNumber());
        Page<String> page = service.findPinCodes(state, district, pageable);
        PagingUtil.pagePostCheck(pageable.getPageNumber(), page);
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent(page, response));
        return page.getContent();
    }

    @GetMapping(ApiPaths.PATH_DISTRICT)
    public List<String> getDistricts(){
       return service.findDistricts();
    }
}
