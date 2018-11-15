package com.ankurpathak.springsessiontest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_FAVICON;

@RestController
public class FaviconRestController {
    @GetMapping(PATH_FAVICON)
    void returnNoFavicon() {
    }

}
