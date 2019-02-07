package com.github.ankurpathak.app;

import com.github.ankurpathak.app.controller.rest.dto.ApiCode;
import com.github.ankurpathak.app.domain.model.Contact;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.require;


@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    public CustomOAuth2UserService(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oath2User = super.loadUser(userRequest);
        return buildPrincipal(oath2User, userRequest.getClientRegistration()).orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error(String.valueOf(ApiCode.OAUTH_LOGIN_ERROR.getCode()))));
    }

    /**
     * Builds the security principal from the given userReqest.
     * Registers the user if not already reqistered
     */
    public Optional<CustomUserDetails> buildPrincipal(OAuth2User oath2User, ClientRegistration client) {
        require(oath2User, notNullValue());
        require(client, notNullValue());
        return findEmail(oath2User.getAttributes(), client)
                .map(email ->
                        CustomUserDetails.getInstance(
                                userDetailsService.findByCandidateKey(email)
                                        .orElseGet(() -> {
                                            User user = User.getInstance()
                                                    .email(Contact.getInstance(email))
                                                    .password(Password.getInstance().value(passwordEncoder.encode(UUID.randomUUID().toString())))
                                                    .enabled(true)
                                                    .roles(Collections.singleton(Role.ROLE_ADMIN));
                                            return userDetailsService.getUserService().create(user);
                                        }),
                                userDetailsService.getPrivileges(Collections.singleton(Role.ROLE_ADMIN)), oath2User));
    }


    private Optional<String> findEmail(Map<String, Object> attributes, ClientRegistration client) {
        switch (client.getRegistrationId()) {
            case CLIENT_FACEBOOK:
                return Optional.ofNullable(MapUtils.getString(attributes, StandardClaimNames.EMAIL));
            case CLIENT_GOOGLE:
                return Optional.ofNullable(MapUtils.getString(attributes, StandardClaimNames.EMAIL));
            case CLIENT_LINKEDIN:
                return Optional.ofNullable(MapUtils.getString(attributes, LinkedinClaims.EMAIL));
            default:
                return Optional.empty();
        }
    }





    public static final String CLIENT_FACEBOOK = "facebook";
    public static final String CLIENT_GOOGLE = "google";
    public static final String CLIENT_LINKEDIN = "linkedin";


    public interface LinkedinClaims {
        String EMAIL = "emailAddress";
    }
}

