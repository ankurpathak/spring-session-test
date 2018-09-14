package com.ankurpathak.springsessiontest;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public abstract class AbstractSocialService implements ISocialService {
    private static  final Logger log = LoggerFactory.getLogger(AbstractSocialService.class);
    private final DefaultApi20 providerApi;
    private OAuth20Service service;

    public AbstractSocialService(DefaultApi20 providerApi) {
        this.providerApi = providerApi;
    }

    protected OAuth20Service service() {
        if(service == null){
            service = new ServiceBuilder(properties().getClientId())
                    .apiSecret(properties().getClientSecret())
                    .scope(properties().getScope()) // replace with desired scope
                    .state(String.format("secret%d", new Random().nextLong()))
                    .callback(properties().getRedirectUrl())
                    .build(providerApi);
        }
        return service;
    }


    public Optional<OAuth2AccessToken> token(String code) {
        try {
            return Optional.of(service().getAccessToken(code));
        } catch (Exception ex){
            log.debug(ex.getMessage());
        }
        return Optional.empty();
    }



    protected abstract SocialProperties properties();


    @Override
    public Optional<String> authorizationUrl() {
        var service = service();
        return Optional.ofNullable(service.getAuthorizationUrl());
    }
}
