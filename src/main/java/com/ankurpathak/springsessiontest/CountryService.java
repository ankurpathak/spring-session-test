package com.ankurpathak.springsessiontest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import java.util.List;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.valid4j.Assertive.ensure;

@Service
public class CountryService implements ICountryService {


    public static final String BASE_URL = "https://restcountries.eu/rest/v2";


    private final WebTarget countryByAlphaTarget;

    public CountryService(
            @Named("countryByAlphaTarget") WebTarget countryByAlphaTarget
    ) {
        this.countryByAlphaTarget = countryByAlphaTarget;
    }


    @Override
    public List<String> alphaCodeToCallingCodes(String alphaCode) {
        //throws  javax.ws.rs.ProcessingException
        ensure(alphaCode, not(isEmptyString()));
        String json = countryByAlphaTarget.queryParam("codes", alphaCode).request().get(String.class);
        DocumentContext context = JsonPathUtil.stringToDoc(json);
        return context.read("$[0].callingCodes", new TypeRef<List<String>>() {
        });
    }


}
