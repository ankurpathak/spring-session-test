package com.github.ankurpathak.api.security.authentication.provider;

import com.github.ankurpathak.api.domain.model.Contact;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.event.SendLoginTokenEvent;
import com.github.ankurpathak.api.security.authentication.token.PreLoginTokenAuthenticationToken;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.primitive.string.StringValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

public class LoginTokenGeneratorAuthenticationProvider implements AuthenticationProvider {
    private  final AuthenticationProvider authenticationProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CustomUserDetailsService userDetailsService;


    public LoginTokenGeneratorAuthenticationProvider(AuthenticationProvider authenticationProvider, ApplicationEventPublisher applicationEventPublisher, CustomUserDetailsService userDetailsService) {
        this.authenticationProvider = authenticationProvider;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userDetailsService = userDetailsService;
    }



    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Optional<DomainContext> domainContext = SecurityUtil.getDomainContext();
        if(domainContext.isPresent()){
            if(domainContext.get().getOtpFlow()){
                try{
                    return authenticationProvider.authenticate(authentication);
                }catch (UsernameNotFoundException uEx) {

                    return processNewPhone(authentication, uEx);
                }catch(BadCredentialsException | DisabledException aEx) {
                    return processExistingPhone(authentication, aEx);
                }
            }else {
                return authenticationProvider.authenticate(authentication);
            }
        }else {
            return authenticationProvider.authenticate(authentication);
        }

    }

    private Authentication processExistingPhone(Authentication authentication, AuthenticationException aEx) {
        if(authentication.getPrincipal() instanceof String){
            String candidateKeyValue = (String) authentication.getPrincipal();
            if(StringValidator.contact(candidateKeyValue, false)){
                Optional<User> user = userDetailsService.getUserService().byPhone(candidateKeyValue);
                if(user.isPresent()){
                    CustomUserDetails userDetails = CustomUserDetails.getInstance(user.get(), userDetailsService.getPrivileges(user.get().getRoles()));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    applicationEventPublisher.publishEvent(new SendLoginTokenEvent(user.get()));
                    return PreLoginTokenAuthenticationToken.getInstance(usernamePasswordAuthenticationToken);
                }else {
                    throw aEx;
                }
            }else {
                throw aEx;
            }
        }else {
            throw aEx;
        }
    }

    private Authentication processNewPhone(Authentication authentication, UsernameNotFoundException uEx) {
        if(authentication.getCredentials() instanceof String){
            String candidateKeyValue = (String) authentication.getPrincipal();
            if(StringValidator.contact(candidateKeyValue, false)){
                User user = registerNewContact(candidateKeyValue);
                CustomUserDetails userDetails = CustomUserDetails.getInstance(user, userDetailsService.getPrivileges(user.getRoles()));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                return PreLoginTokenAuthenticationToken.getInstance(usernamePasswordAuthenticationToken);
            }else {
                throw uEx;
            }
        }else {
            throw uEx;
        }
    }


    private User registerNewContact(String candidateKeyValue){
        User user = User.getInstance()
                .phone(Contact.getInstance(candidateKeyValue))
                .roles(Collections.singleton(Role.ROLE_ADMIN))
                .enabled(false);
        userDetailsService.getUserService().create(user);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user));
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }


}