package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.SendLoginTokenEvent;
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
public class SendLoginTokenEventListener implements ApplicationListener<SendLoginTokenEvent> {
    private static final Logger log = LoggerFactory.getLogger(SendLoginTokenEventListener.class);


    private final CustomUserDetailsService userDetailsService;
    private final ISmsService smsService;
    private final ITokenService tokenService;

    public SendLoginTokenEventListener(CustomUserDetailsService userDetailsService, ISmsService smsService, ITokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.smsService = smsService;
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(SendLoginTokenEvent event) {
        Optional.ofNullable(event.getSource())
                .ifPresentOrElse(user -> {
                    Optional.ofNullable(user.getPhone())
                            .ifPresentOrElse(phone -> {
                                Optional.ofNullable(phone.getValue())
                                        .ifPresentOrElse(phoneValue -> {
                                            tokenService.findPhoneToken(phoneValue)
                                                    .ifPresentOrElse(token -> {
                                                        if (Instant.now().isBefore(token.getExpiry())) {
                                                            smsService.sendLoginToken(user, token);
                                                        } else {
                                                            log.info("Token: {} expired", token.getId());
                                                        }
                                                    }, () -> {
                                                        tokenService.generatePhoneToken(phoneValue)
                                                                .ifPresentOrElse(token -> {
                                                                    userDetailsService.getUserService().update(user);
                                                                    smsService.sendLoginToken(user, token);
                                                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                    });
                                        }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE_VALUE, String.valueOf(user.getId()))); // phoneValueNull

                            }, ()-> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE, String.valueOf(user.getId()))); //PhoneNull
                }, () -> LogUtil.logNull(log, User.class.getSimpleName()));
    }
}
