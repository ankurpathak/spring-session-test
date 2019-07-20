package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.LoginTokenEvent;
import com.github.ankurpathak.api.event.util.EventUtil;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.ISmsService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class LoginTokenEventListener implements ApplicationListener<LoginTokenEvent> {
    private static final Logger log = LoggerFactory.getLogger(LoginTokenEventListener.class);


    private final CustomUserDetailsService userDetailsService;
    private final ISmsService smsService;
    private final ITokenService tokenService;

    public LoginTokenEventListener(CustomUserDetailsService userDetailsService, ISmsService smsService, ITokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.smsService = smsService;
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(LoginTokenEvent event) {
        Optional.ofNullable(event.getSource())
                .ifPresentOrElse(user -> {
                    EventUtil.sendPhoneToken(user, tokenService, smsService::sendLoginToken, log);
                }, () -> LogUtil.logNull(log, User.class.getSimpleName()));
    }
}
