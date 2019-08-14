package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.IEmailTemplateService;
import com.github.ankurpathak.api.service.dto.EmailContext;
import com.github.ankurpathak.api.service.dto.SmtpContext;
import com.github.ankurpathak.api.domain.model.SmtpCredential;
import com.github.ankurpathak.api.service.impl.util.EmailUtil;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.github.ankurpathak.api.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static com.github.ankurpathak.api.service.dto.EmailMessages.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

/**
 * Created by ankur on 29-05-2017.
 */
@Service
public class EmailService implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    //Messages

    //Messages

    //Autowiring
    private final JavaMailSender javaMailSender;
    private final Environment environment;
    private final MessageSource messageSource;
    private final IEmailTemplateService emailTemplateService;
    private final TaskExecutor taskExecutor;
    private final Validator validator;
    private final MailService mailService;

    public EmailService(JavaMailSender javaMailSender, Environment environment, MessageSource messageSource, IEmailTemplateService emailTemplateService, TaskExecutor taskExecutor, Validator validator, MailService mailService) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
        this.messageSource = messageSource;
        this.emailTemplateService = emailTemplateService;
        this.taskExecutor = taskExecutor;
        this.validator = validator;
        this.mailService = mailService;
    }
    //Autowiring

    //Property
    //Property



    @Override
    public void sendForAccountEnable(User user, Token token) {
        require(user, notNullValue());
        require(token, notNullValue());
        String createAccountEnableHtml = null;
        try {
            createAccountEnableHtml = emailTemplateService.createAccountEnableHtml(user, token);
        } catch (RuntimeException ex) {
            log.info("Problem in creating account email html. message: {} cause: {}", ex.getMessage(), ex.getCause());
        }

        if (!StringUtils.isEmpty(createAccountEnableHtml)) {
            sendUserEmail(user, ACCOUNT_ENABLE_SUBJECT, createAccountEnableHtml);
        }
    }

    @Override
    public void sendForBusinessAdded(User user, Business business) {
        require(user, notNullValue());
        require(business, notNullValue());
        String createBusinessAddedHtml = null;
        try {
            createBusinessAddedHtml = emailTemplateService.createBusinessAddedHtml(user, business);
        } catch (RuntimeException ex) {
            log.info("Problem in creating business email html. message: {} cause: {}", ex.getMessage(), ex.getCause());
        }

        if (!StringUtils.isEmpty(createBusinessAddedHtml)) {
            sendUserEmail(user, BUSINESS_ADDED_SUBJECT, createBusinessAddedHtml);
        }
    }

    @Override
    public void sendForForgetPassword(User user, Token token) {
        require(user, notNullValue());
        require(token, notNullValue());
        String createForgetPasswordHtml = null;
        try {
            createForgetPasswordHtml = emailTemplateService.createForgetPasswordHtml(user, token);
        } catch (RuntimeException ex) {
            log.info("Problem in creating forget password html. message: {} cause: {}", ex.getMessage(), ex.getCause());
        }

        if (!StringUtils.isEmpty(createForgetPasswordHtml)) {
            sendUserEmail(user, FORGET_PASSWORD_SUBJECT, createForgetPasswordHtml);
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
        String from = EmailUtil.getFrom(EmailUtil.getSmtpCredential(null), environment);
        EmailContext emailContext = new EmailContext(subject, email, from, body, Collections.emptyList(), null,Collections.emptyList());

        EmailUtil.sendSimpleMessage(new SmtpContext(Collections.singletonList(emailContext), sender));
    }





    private void sendUserEmail(User user, String subjectKey, String html) {
        require(user, notNullValue());
        require(user.getEmail(), notNullValue());
        String email = user.getEmail().getValue();
        sendEmail(email, subjectKey, html, user);
    }




    private void sendEmail(String email, String subjectKey, String html, Domain<?> to){
        String subject = MessageUtil.getMessage(messageSource, subjectKey);
        JavaMailSender sender = getJavaMailSender(SmtpCredential.EMPTY_INSTANCE);
        String from = EmailUtil.getFrom(EmailUtil.getSmtpCredential(null), environment);
        if (!StringUtils.isEmpty(email)) {
            EmailContext emailContextUser = new EmailContext(subject, email, from, html, Collections.emptyList(), null,Collections.emptyList());
            mailService.processEmailContext(emailContextUser, to);
            EmailUtil.sendMimeMail(mailService, taskExecutor, new SmtpContext(Collections.singletonList(emailContextUser), sender));
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
