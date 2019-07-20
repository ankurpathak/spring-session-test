package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.service.ILinkedinService;
import com.github.ankurpathak.api.service.dto.LinkedinProperties;
import com.github.ankurpathak.api.service.dto.SocialProfile;
import com.github.ankurpathak.api.service.dto.SocialProperties;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class LinkedinService extends AbstractSocialService implements ILinkedinService {

    private static  final Logger log = LoggerFactory.getLogger(LinkedinService.class);


    private final LinkedinProperties linkedinProperties;

    public static final String USER_INFO_URL = "https://api.linkedin.com/v1/people/";



    public LinkedinService(LinkedinProperties linkedinProperties) {
        super(LinkedInApi20.instance());
        this.linkedinProperties = linkedinProperties;
    }

    @Override
    protected SocialProperties properties() {
        return linkedinProperties;
    }

    @Override
    public Optional<String> accessToken(String code) {
        return token(code).map(OAuth2AccessToken::getAccessToken);
    }

    @Override
    public Optional<SocialProfile> socialProfile(String token) {
        OAuth20Service service = service();
        String url = UriComponentsBuilder.fromUriString(USER_INFO_URL)
                .path("~:(id,email-address,first-name,last-name,picture-url)")
                .queryParam("format", "json")
                .toUriString();
        final OAuthRequest request = new OAuthRequest(Verb.GET, url);
        service.signRequest(token, request);
        try {
            Response response = service.execute(request);
            Configuration config = Configuration.defaultConfiguration()
                    .addOptions(Option.SUPPRESS_EXCEPTIONS);
            var doc = JsonPath.using(config).parse(response.getBody());
            SocialProfile profile = SocialProfile.getInstance()
                    .firstName(doc.read("$.firstName"))
                    .lastName(doc.read("$.lastName"))
                    .email(doc.read("$.emailAddress"))
                    .imgUrl(doc.read( "$.pictureUrl"));
            return Optional.of(profile);
        }catch (Exception ex){
            log.debug(ex.getMessage());
            return Optional.empty();
        }


    }
}
