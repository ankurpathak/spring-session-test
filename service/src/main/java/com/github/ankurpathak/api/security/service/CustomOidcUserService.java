package com.github.ankurpathak.api.security.service;


import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

//@Component
public class CustomOidcUserService extends OidcUserService {

    private final CustomOAuth2UserService oauth2UserService;

    public CustomOidcUserService(CustomOAuth2UserService oauth2UserService) {
        this.oauth2UserService = oauth2UserService;
    }


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        return oauth2UserService.buildPrincipal(oidcUser, userRequest.getClientRegistration())
                .map(userDetails -> CustomUserDetails.getInstance(userDetails, oidcUser))
                .orElseThrow(()-> new OAuth2AuthenticationException(new OAuth2Error(String.valueOf(ApiCode.OAUTH_LOGIN_ERROR.getCode()))));
    }
}
