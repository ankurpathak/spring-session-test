package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.EmailTokenEvent;
import com.github.ankurpathak.api.event.util.EventUtil;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;


@Component
public class EmailTokenEventListener implements ApplicationListener<EmailTokenEvent> {
    private static final Logger log = LoggerFactory.getLogger(EmailTokenEventListener.class);

    private final ITokenService tokenService;
    private final IEmailService emailService;

    public EmailTokenEventListener(ITokenService tokenService, IEmailService emailService) {
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(EmailTokenEvent event) {
        require(event, notNullValue());
        Optional.ofNullable(event.getSource())
                .ifPresentOrElse(user -> {
                    EventUtil.sendAccountEnableToken(user, tokenService, emailService::sendForAccountEnable, log);
                }, () -> LogUtil.logNull(log, User.class.getSimpleName()));
    }
}
