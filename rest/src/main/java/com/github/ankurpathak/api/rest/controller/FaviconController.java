package com.github.ankurpathak.api.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.ankurpathak.api.constant.RequestMappingPaths.PATH_FAVICON;

@RestController
public class FaviconController {
    @GetMapping(PATH_FAVICON)
    void returnNoFavicon() {
    }

}
