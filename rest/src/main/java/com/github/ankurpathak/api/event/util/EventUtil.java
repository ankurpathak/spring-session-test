package com.github.ankurpathak.api.event.util;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.ISmsService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.Optional;

public class EventUtil {


    public static void  sendAccountEnableToken(User user, ITokenService tokenService, ITokenService.ISendUserToken send, Logger log){
                    Optional.ofNullable(user.getEmail())
                            .ifPresentOrElse(email -> {
                                Optional.ofNullable(email.getValue())
                                        .ifPresentOrElse(emailValue -> {
                                            tokenService.findAccountToken(emailValue)
                                                    .ifPresentOrElse(token -> {
                                                        if (Instant.now().isBefore(token.getExpiry())) {
                                                            tokenService.findAccountToken(token.getValue())
                                                                    .ifPresentOrElse(reverseToken -> {
                                                                        send.sendToken(user, reverseToken);
                                                                    }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                        } else {
                                                            log.info("Token: {} expired", token.getId());
                                                        }

                                                    }, () -> {
                                                        tokenService.generateAccountToken(email.getValue())
                                                                .ifPresentOrElse(token -> {
                                                                    tokenService.findAccountToken(token.getValue())
                                                                            .ifPresentOrElse(reverseToken -> {
                                                                                send.sendToken(user, reverseToken);
                                                                            }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                    });
                                        }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL_VALUE, String.valueOf(user.getId())));
                            }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId())));
    }


    public static void  sendPhoneToken(User user, ITokenService tokenService, ITokenService.ISendUserToken send, Logger log){
        Optional.ofNullable(user.getPhone())
                .ifPresentOrElse(phone -> {
                    Optional.ofNullable(phone.getValue())
                            .ifPresentOrElse(phoneValue -> {
                                tokenService.findPhoneToken(phoneValue)
                                        .ifPresentOrElse(token -> {
                                            if (Instant.now().isBefore(token.getExpiry())) {
                                                send.sendToken(user, token);
                                            } else {
                                                log.info("Token: {} expired", token.getId());
                                            }
                                        }, () -> {
                                            tokenService.generatePhoneToken(phoneValue)
                                                    .ifPresentOrElse(token -> {
                                                        send.sendToken(user, token);
                                                    }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                        });
                            }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE_VALUE, String.valueOf(user.getId())));
                }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE, String.valueOf(user.getId())));
    }
}
