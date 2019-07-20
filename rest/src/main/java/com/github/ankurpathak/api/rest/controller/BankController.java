package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.service.IBankService;
import com.github.ankurpathak.api.service.IMessageService;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.QueryParam;
import java.util.List;

@ApiController
public class BankController {

    private final IBankService service;
    private final IMessageService messageService;

    public BankController(IBankService service, IMessageService messageService) {
        this.service = service;
        this.messageService = messageService;
    }


    @GetMapping(ApiPaths.PATH_BANK)
    public List<Document> getNames(){
        return service.findNames();
    }


    @GetMapping(ApiPaths.PATH_BANK_IFSC)
    public ResponseEntity<?> getBank(@PathVariable(Params.Path.IFSC) String ifsc){
        return ControllerUtil.processOptional(service.findBranch(ifsc), null, "Bank", ifsc, messageService);
    }

    @GetMapping(ApiPaths.PATH_BANK_STATE)
    public List<String> getStates(@QueryParam(Params.Query.CODE) String code){
        return service.findStates(code);
    }

    @GetMapping(ApiPaths.PATH_BANK_DISTRICT)
    public List<String> getDistrict(@QueryParam(Params.Query.CODE) String code, @QueryParam(Params.Query.STATE) String state){
        return service.findDistrict(code, state);
    }

    @GetMapping(ApiPaths.PATH_BANK_BRANCH)
    public List<Document> getBranch(@QueryParam(Params.Query.CODE) String code, @QueryParam(Params.Query.STATE) String state, @QueryParam(Params.Query.DISTRICT) String district){
        return service.findBranches(code, state, district);
    }
}
