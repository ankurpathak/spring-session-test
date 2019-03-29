package com.github.ankurpathak.app;

import com.github.ankurpathak.app.service.IFacebookService;
import com.github.ankurpathak.app.service.IGoogleService;
import com.github.ankurpathak.app.service.ILinkedinService;
import com.github.ankurpathak.app.service.ISocialService;
import com.github.ankurpathak.app.service.dto.SocialProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Optional;

public class SocialWebAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    final static Logger log = LoggerFactory.getLogger(SocialWebAuthenticationProvider.class);

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private volatile String userNotFoundEncodedPassword;
    protected final UserDetailsService userDetailsService;
    protected final PasswordEncoder passwordEncoder;
    protected final IGoogleService googleService;
    protected final IFacebookService facebookService;
    protected final ILinkedinService linkedinService;

    public SocialWebAuthenticationProvider(UserDetailsService userDetailsService, IGoogleService googleService, IFacebookService facebookService, ILinkedinService linkedinService) {
        this.googleService = googleService;
        this.userDetailsService  = userDetailsService;
        this.facebookService = facebookService;
        this.linkedinService = linkedinService;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        super.hideUserNotFoundExceptions = false;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }


    public boolean supports(Class<?> authentication) {
        return SocialWebAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported"));
        String provider = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
        Optional<SocialProfile> profile = tryToFetchSocialProofile(provider, authentication);
        boolean cacheWasUsed = true;
        if(profile.isPresent()) {
            UserDetails user = super.getUserCache().getUserFromCache(profile.get().getEmail());
            if (user == null) {
                cacheWasUsed = false;

                try {
                    user = this.retrieveUser(profile.get().getEmail(), (UsernamePasswordAuthenticationToken) authentication);
                } catch (UsernameNotFoundException var6) {
                    this.logger.debug("User '" + profile.get().getEmail() + "' not found");
                    if (this.hideUserNotFoundExceptions) {
                        throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                    }
                    throw new SocialProfileNotFoundException(var6.getMessage(), var6, profile.get());
                }

                Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
            }

            try {
                super.getPreAuthenticationChecks().check(user);
                this.additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken)authentication);
            } catch (AuthenticationException var7) {
                if (!cacheWasUsed) {
                    throw var7;
                }

                cacheWasUsed = false;
                //user = this.retrieveUser(profile.get().getEmail(), (UsernamePasswordAuthenticationToken)authentication);
                try {
                    user = this.retrieveUser(profile.get().getEmail(), (UsernamePasswordAuthenticationToken) authentication);
                } catch (UsernameNotFoundException var6) {
                    this.logger.debug("User '" + profile.get().getEmail() + "' not found");
                    throw new SocialProfileNotFoundException(var6.getMessage(), var6, profile.get());
                }
                this.getPreAuthenticationChecks().check(user);
                this.additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken)authentication);
            }

            this.getPostAuthenticationChecks().check(user);
            if (!cacheWasUsed) {
                this.getUserCache().putUserInCache(user);
            }

            Object principalToReturn = user;
            if (this.isForcePrincipalAsString()) {
                principalToReturn = user.getUsername();
            }

            return this.createSuccessAuthentication(principalToReturn, authentication, user);
        }else {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }


    }

    private Optional<SocialProfile> tryToFetchSocialProofile(String provider, Authentication authentication) {
        Optional<SocialProfile> profile = Optional.empty();
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String code = authentication.getCredentials().toString();
            switch (provider) {
                case ISocialService.GOOGLE:
                    profile = googleService.idToken(code)
                            .flatMap(googleService::socialProfile);
                    break;
                case ISocialService.FACEBOOK:
                    profile = facebookService.accessToken(code)
                            .flatMap(facebookService::socialProfile);
                    break;
                case ISocialService.LINKEDIN:
                    profile = linkedinService.accessToken(code)
                            .flatMap(linkedinService::socialProfile);
                    break;
                default:
                    break;
            }
        }
        return profile;
    }


    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.prepareTimingAttackProtection();

        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                return loadedUser;
            }
        } catch (UsernameNotFoundException var4) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var4;
        } catch (InternalAuthenticationServiceException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
        }

    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }

    }


    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }
}
