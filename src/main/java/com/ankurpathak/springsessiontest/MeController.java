package com.ankurpathak.springsessiontest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MeController {

    @GetMapping("/me")
    public Principal me(Principal principal){
        return principal;
    }
}
