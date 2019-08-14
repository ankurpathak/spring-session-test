package com.github.ankurpathak.api.service.impl.util;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import com.github.ankurpathak.api.service.IMailService;
import com.github.ankurpathak.api.service.dto.EmailAttachmentContext;
import com.github.ankurpathak.api.service.dto.EmailContext;
import com.github.ankurpathak.api.service.dto.SmtpContext;
import com.github.ankurpathak.api.domain.model.SmtpCredential;
import com.github.ankurpathak.api.service.impl.EmailService;
import com.github.ankurpathak.api.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;
import static org.valid4j.Assertive.require;

public class EmailUtil {

    public static final String PROPERTY_EMAIL_FROM = "api.email.format";

    private static  final Logger log = LoggerFactory.getLogger(EmailUtil.class);


    public static SmtpCredential getSmtpCredential(Business business){
        return Optional.ofNullable(business).map(Business::getSmtpCredential).orElse(SmtpCredential.EMPTY_INSTANCE);
    }


    public static JavaMailSender getLocalMailSender(Environment environment, SmtpCredential smtpCredential) {
        require(smtpCredential, notNullValue());
        require(smtpCredential.getEmail(), notNullValue());
        require(smtpCredential.getPassword(), notNullValue());

        JavaMailSenderImpl selfJavaMailSender = new JavaMailSenderImpl();
        selfJavaMailSender.setHost(PropertyUtil.getProperty(environment, "spring.mail.host"));
        selfJavaMailSender.setPort(PropertyUtil.getProperty(environment, "spring.mail.port", int.class, 0));
        selfJavaMailSender.setUsername(smtpCredential.getUsername());
        selfJavaMailSender.setPassword(smtpCredential.getPassword());
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", PropertyUtil.getProperty(environment, "spring.mail.properties.mail.smtp.auth", boolean.class, false));
        properties.put("mail.debug", PropertyUtil.getProperty(environment, "spring.mail.properties.mail.debug", boolean.class, false));
        properties.put("mail.smtp.socketFactory.fallback", PropertyUtil.getProperty(environment, "spring.mail.properties.mail.smtp.socketFactory.fallback", boolean.class, false));
        properties.put("mail.smtp.starttls.enable", PropertyUtil.getProperty(environment, "spring.mail.properties.mail.smtp.starttls.enable", boolean.class, false));
        selfJavaMailSender.setJavaMailProperties(properties);
        return selfJavaMailSender;
    }


    public static MimeMessage convertEmailDtoToMimeMessage(JavaMailSender sender, EmailContext emailContext) throws Exception {
        require(sender, notNullValue());
        require(emailContext, notNullValue());
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        //messageHelper.setFrom();
        messageHelper.setFrom(emailContext.getFrom());
        messageHelper.setTo(emailContext.getTo());
        messageHelper.setSubject(emailContext.getSubject());
        messageHelper.setText(emailContext.getBody(), emailContext.isHtml());
        if (!CollectionUtils.isEmpty(emailContext.getEmailAttachments()))
            for (EmailAttachmentContext dataSource : emailContext.getEmailAttachments()) {
                messageHelper.addAttachment(dataSource.getOriginalFileName(), dataSource.getDataSource());
            }
        if (!StringUtils.isEmpty(emailContext.getReplyTo()))
            messageHelper.setReplyTo(emailContext.getReplyTo());
        if (!CollectionUtils.isEmpty(emailContext.getCcs()))
            for (String cc : emailContext.getCcs())
                messageHelper.addCc(cc);
        return message;
    }


    public static SimpleMailMessage convertEmailDtoToSimpleMessage(JavaMailSender sender, EmailContext emailContext) throws Exception {
        require(sender, notNullValue());
        require(emailContext, notNullValue());
        SimpleMailMessage message = new SimpleMailMessage();
        //messageHelper.setFrom();
        message.setFrom(emailContext.getFrom());
        message.setTo(emailContext.getTo());
        message.setSubject(emailContext.getSubject());
        message.setText(emailContext.getBody());

        if (!StringUtils.isEmpty(emailContext.getReplyTo()))
            message.setReplyTo(emailContext.getReplyTo());
        if (!CollectionUtils.isEmpty(emailContext.getCcs()))
            message.setCc(emailContext.getCcs().toArray(new String[0]));
        return message;
    }


    public static String getFrom(SmtpCredential smtpCredential, Environment environment) {
        require(smtpCredential, notNullValue());
        require(environment, notNullValue());
        if (!StringUtils.isEmpty(smtpCredential.getEmail())){
            if(!StringUtils.isEmpty(smtpCredential.getName())){
                return String.format("%s<%s>",smtpCredential.getName(), smtpCredential.getEmail());
            }else
                return smtpCredential.getEmail();
        }
        else
            return PropertyUtil.getProperty(environment, PROPERTY_EMAIL_FROM);
     }


    public static void sendMimeMail(IMailService mailService, TaskExecutor taskExecutor, SmtpContext smtpContext) {
        require(smtpContext, notNullValue());
        Optional<DomainContext> domainContext = DomainContextHolder.getContext();
        if(domainContext.isPresent() && domainContext.get().isAsync()){
            taskExecutor.execute(() -> {
                processSmtpContextWithMimeMessage(smtpContext);
            });
        }else {
            processSmtpContextWithMimeMessage(smtpContext);
        }


    }

    public static void processSmtpContextWithMimeMessage(SmtpContext smtpContext){
        require(smtpContext, notNullValue());
        List<EmailContext> emailContexts = smtpContext.getEmails();
        if (!CollectionUtils.isEmpty(smtpContext.getEmails()))
            for (EmailContext emailContext : emailContexts) {
                try {
                    MimeMessage message = EmailUtil.convertEmailDtoToMimeMessage(smtpContext.getSender(), emailContext);
                    smtpContext.getSender().send(message);


                    log.info("Successfully sent email to {}.", emailContext.getTo());
                } catch (Exception ex) {
                    log.info("Problem in sending email to {}. message: {} cause: {}", emailContext.getTo(), ex.getMessage(), ex.getCause());
                }
            }
    }


    public static void sendSimpleMessage(SmtpContext smtpContext) {
        require(smtpContext, notNullValue());
            List<EmailContext> emailContexts = smtpContext.getEmails();
            if (!CollectionUtils.isEmpty(smtpContext.getEmails()))
                for (EmailContext emailContext : emailContexts) {
                    try {
                        SimpleMailMessage message = EmailUtil.convertEmailDtoToSimpleMessage(smtpContext.getSender(), emailContext);
                        smtpContext.getSender().send(message);
                    } catch (Exception ex) {
                        log.info("Problem in sending email to {}. message: {} cause: {}", emailContext.getTo(), ex.getMessage(), ex.getCause());
                    }
                }



    }







}
