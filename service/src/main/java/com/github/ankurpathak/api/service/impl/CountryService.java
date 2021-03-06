package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.util.JsonPathUtil;
import com.github.ankurpathak.api.service.ICountryService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.WebTarget;
import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.valid4j.Assertive.require;

@Service
public class CountryService implements ICountryService {
    public static final String BASE_URL = "https://restcountries.eu/rest/v2";


    private final WebTarget countryByAlphaTarget;

    public CountryService(
            @Qualifier("countryByAlphaTarget") WebTarget countryByAlphaTarget
    ) {
        this.countryByAlphaTarget = countryByAlphaTarget;
    }



    public List<String> alphaCodeToCallingCodes(String alphaCode) {
        //throws  javax.ws.rs.ProcessingException
        require(alphaCode, not(emptyString()));
        String json = countryByAlphaTarget.queryParam("codes", alphaCode).request().get(String.class);
        DocumentContext context = JsonPathUtil.stringToDoc(json);
        return context.read("$[0].callingCodes", new TypeRef<>() {
        });
    }









}
