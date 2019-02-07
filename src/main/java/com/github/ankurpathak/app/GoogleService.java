package com.github.ankurpathak.app;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ankurpathak.app.service.impl.AbstractSocialService;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleService extends AbstractSocialService implements IGoogleService {


    private static  final  Logger log = LoggerFactory.getLogger(GoogleService.class);

    public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";


    private final GoogleProperties properties;

    public GoogleService(GoogleProperties properties) {
        super(GoogleApi20.instance());
        this.properties = properties;
    }


    @Override
    public Optional<String> accessToken(String code) {
        return token(code).map(x -> (OpenIdOAuth2AccessToken)x).map(OpenIdOAuth2AccessToken::getAccessToken);
    }


    @Override
    public Optional<String> idToken(String code) {
        return token(code).map(x -> (OpenIdOAuth2AccessToken)x).map(OpenIdOAuth2AccessToken::getOpenIdToken);
    }

    @Override
    public Optional<SocialProfile> socialProfile(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Optional.of(
                    SocialProfile.getInstance()
                            .firstName(jwt.getClaim("given_name").asString())
                            .lastName(jwt.getClaim("family_name").asString())
                            .email(jwt.getClaim("email").asString())
                            .imgUrl("picture")
            );

        } catch (JWTDecodeException e) {
            log.debug(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    protected SocialProperties properties() {
        return properties;
    }
}
