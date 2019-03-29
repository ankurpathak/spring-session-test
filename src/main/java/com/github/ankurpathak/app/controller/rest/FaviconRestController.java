package com.github.ankurpathak.app.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.ankurpathak.app.RequestMappingPaths.PATH_FAVICON;

@RestController
public class FaviconRestController {
    @GetMapping(PATH_FAVICON)
    void returnNoFavicon() {
    }

}
