package com.github.ankurpathak.app.security.core;

import com.github.ankurpathak.app.service.IFacebookService;
import com.github.ankurpathak.app.service.IGoogleService;
import com.github.ankurpathak.app.service.ILinkedinService;
import com.github.ankurpathak.app.service.ISocialService;
import com.github.ankurpathak.app.service.dto.SocialProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public class SocialApplicationAuthenticationProvider extends SocialWebAuthenticationProvider {
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private volatile String userNotFoundEncodedPassword;

    final static Logger log = LoggerFactory.getLogger(SocialApplicationAuthenticationProvider.class);


    public SocialApplicationAuthenticationProvider(UserDetailsService userDetailsService, IGoogleService googleService, IFacebookService facebookService, ILinkedinService linkedinService) {
        super(userDetailsService,googleService,facebookService,linkedinService);
    }


    private Optional<SocialProfile> tryToFetchSocialProofile(String provider, Authentication authentication) {
        Optional<SocialProfile> profile = Optional.empty();
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String token = authentication.getCredentials().toString();
            switch (provider) {
                case ISocialService.GOOGLE:
                    profile = googleService.socialProfile(token);
                    break;
                case ISocialService.FACEBOOK:
                    profile = facebookService.socialProfile(token);
                    break;
                case ISocialService.LINKEDIN:
                    profile = linkedinService.socialProfile(token);
                    break;
                default:
                    break;
            }
        }
        return profile;
    }

    public boolean supports(Class<?> authentication) {
        return SocialApplicationAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
