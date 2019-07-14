package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.IEmailTemplateService;
import com.github.ankurpathak.api.service.dto.EmailContext;
import com.github.ankurpathak.api.service.dto.SmtpContext;
import com.github.ankurpathak.api.service.dto.SmtpCredential;
import com.github.ankurpathak.api.service.impl.util.EmailUtil;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.github.ankurpathak.api.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

/**
 * Created by ankur on 29-05-2017.
 */
@Service
public class EmailService implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    //Messages
    private static final String MESSAGE_ACCOUNT_ENABLE_SUBJECT = "api.message.register-enable-subject";
    private static final String MESSAGE_FORGET_PASSWORD_SUBJECT = "api.message.forget-password-subject";
    //Messages

    //Autowiring
    private final JavaMailSender javaMailSender;
    private final Environment environment;
    private final MessageSource messageSource;
    private final IEmailTemplateService emailTemplateService;
    private final TaskExecutor taskExecutor;
    private final Validator validator;

    public EmailService(JavaMailSender javaMailSender, Environment environment, MessageSource messageSource, IEmailTemplateService emailTemplateService, TaskExecutor taskExecutor, Validator validator) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
        this.messageSource = messageSource;
        this.emailTemplateService = emailTemplateService;
        this.taskExecutor = taskExecutor;
        this.validator = validator;
    }
    //Autowiring

    //Property
    //Property



    @Override
    @Async
    public void sendForAccountEnable(User user, Token token, boolean async) {
        require(user, notNullValue());
        require(token, notNullValue());
        String createAccountEnableHtml = null;
        try {
            createAccountEnableHtml = emailTemplateService.createAccountEnableHtml(user, token);
        } catch (RuntimeException ex) {
            log.info("Problem in creating register email html. message: {} cause: {}", ex.getMessage(), ex.getCause());
        }

        if (!StringUtils.isEmpty(createAccountEnableHtml)) {
            sendUserEmail(user, MESSAGE_ACCOUNT_ENABLE_SUBJECT, createAccountEnableHtml, async);
        }
    }

    @Override
    public void sendForForgetPassword(User user, Token token, boolean async) {
        require(user, notNullValue());
        require(token, notNullValue());
        String createForgetPasswordHtml = null;
        try {
            createForgetPasswordHtml = emailTemplateService.createForgetPasswordHtml(user, token);
        } catch (RuntimeException ex) {
            log.info("Problem in creating register email html. message: {} cause: {}", ex.getMessage(), ex.getCause());
        }

        if (!StringUtils.isEmpty(createForgetPasswordHtml)) {
            sendUserEmail(user, MESSAGE_FORGET_PASSWORD_SUBJECT, createForgetPasswordHtml, async);
        }

    }

    @Override
    public void sendTextEmail(String email, String subject, String body, String replyTo, String... ccs) {
        require(email, MatcherUtil.notStringEmpty());
        require(subject, notNullValue());
        require(subject, notNullValue());
        sendSimpleMessage(email, subject, body, replyTo, ccs);
    }



    @Override
    public void sendTextEmail(String email, String subject, String body) {
        sendTextEmail(email, subject, body, null);
    }



    private void sendSimpleMessage(String email, String subject, String body, String replyTo, String... ccs){
        JavaMailSender sender = getJavaMailSender(SmtpCredential.EMPTY_INSTANCE);
        String from = EmailUtil.getFrom(SmtpCredential.EMPTY_INSTANCE, environment);
        EmailContext emailContext = new EmailContext(subject, email, from, body, null, null, null);
        EmailUtil.sendSimpleMessage(new SmtpContext(Collections.singletonList(emailContext), sender));
    }





    private void sendUserEmail(User user, String subjectKey, String html, boolean async) {
        require(user, notNullValue());
        require(user.getEmail(), notNullValue());
        String email = user.getEmail().getValue();
        String subject = MessageUtil.getMessage(messageSource, subjectKey);
        JavaMailSender sender = getJavaMailSender(SmtpCredential.EMPTY_INSTANCE);
        String from = EmailUtil.getFrom(SmtpCredential.EMPTY_INSTANCE, environment);
        if (!StringUtils.isEmpty(email)) {
            EmailContext emailContextUser = new EmailContext(subject, email, from, html, null, null, null);
            EmailUtil.sendMimeMail(taskExecutor, new SmtpContext(Collections.singletonList(emailContextUser), sender, async));
        }
        disposeJavaMailSender(sender);
    }


    private JavaMailSender getJavaMailSender(SmtpCredential credential) {
        Set<ConstraintViolation<SmtpCredential>> violations = validator.validate(credential);
        validator.validate(credential);
        if (CollectionUtils.isEmpty(violations))
            return EmailUtil.getLocalMailSender(environment, credential);
        return javaMailSender;
    }


    private void disposeJavaMailSender(JavaMailSender javaMailSender) {
        if (!Objects.equals(javaMailSender, this.javaMailSender)) {
            javaMailSender = null;
            Runtime.getRuntime().gc();
        }
    }


}