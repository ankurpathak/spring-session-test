package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.model.VUserBusiness;
import com.github.ankurpathak.api.rest.controller.dto.View;
import org.springframework.web.bind.annotation.GetMapping;

@ApiController
public class MeController {

    @GetMapping(ApiPaths.PATH_ME)
    @JsonView(View.Me.class)
    public VUserBusiness get(@CurrentUser VUserBusiness user){
        return user;
    }

}
