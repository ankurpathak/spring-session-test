package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.BusinessAddedEvent;
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
public class BusinessAddedEventListener implements ApplicationListener<BusinessAddedEvent> {

    private static final Logger log = LoggerFactory.getLogger(BusinessAddedEventListener.class);
    private final IEmailService emailService;

    public BusinessAddedEventListener(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(BusinessAddedEvent event) {
        require(event, notNullValue());
        Optional.ofNullable(event.getSource())
                .ifPresentOrElse(business -> {
                    Optional.ofNullable(event.getUser())
                            .ifPresentOrElse(user -> {
                                emailService.sendForBusinessAdded(user, business);
                            }, () -> LogUtil.logNull(log, User.class.getSimpleName()));

                }, () -> LogUtil.logNull(log, Business.class.getSimpleName()));
    }
}

