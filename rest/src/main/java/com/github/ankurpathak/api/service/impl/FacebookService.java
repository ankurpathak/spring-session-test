package com.github.ankurpathak.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.service.IFacebookService;
import com.github.ankurpathak.api.service.dto.SocialProfile;
import com.github.ankurpathak.api.service.dto.SocialProperties;
import com.github.ankurpathak.api.service.dto.FacebookProperties;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class FacebookService extends AbstractSocialService implements IFacebookService {

    private final FacebookProperties properties;
    private final ObjectMapper objectMapper;

    private static  final Logger log = LoggerFactory.getLogger(FacebookService.class);


    public static final String USER_INFO_URL = "https://graph.facebook.com/me";

    public FacebookService(FacebookProperties properties, ObjectMapper objectMapper) {
        super(FacebookApi.customVersion("3.1"));
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected SocialProperties properties() {
        return properties;
    }

    @Override
    public Optional<String> accessToken(String code) {
        return token(code).map(OAuth2AccessToken::getAccessToken);
    }


    @Override
    public Optional<SocialProfile> socialProfile(String token) {
        String url  = UriComponentsBuilder.fromUriString(USER_INFO_URL)
                .queryParam("fields", "first_name,last_name,email,picture.type(large)")
                .build()
                .encode()
                .toUriString();
        final OAuthRequest request = new OAuthRequest(Verb.GET, url);
        var service = service();
        service.signRequest(token, request);
        try{
            Response response = service.execute(request);
            Configuration config = Configuration.defaultConfiguration()
                    .addOptions(Option.SUPPRESS_EXCEPTIONS);
            var doc = JsonPath.using(config).parse(response.getBody());
            SocialProfile profile = SocialProfile.getInstance()
                    .firstName(doc.read("$.first_name"))
                    .lastName(doc.read("$.last_name"))
                    .email(doc.read("$.email"))
                    .imgUrl(doc.read( "$.picture.data.url"));
            return Optional.of(profile);
        }catch (Exception ex){
            return Optional.empty();
        }

    }
}
