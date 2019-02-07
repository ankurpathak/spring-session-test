package com.github.ankurpathak.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class EmailUtil {

    public static final String PROPERTY_EMAIL_FROM = "api.email.format";

    private static  final Logger log = LoggerFactory.getLogger(EmailUtil.class);


    public static JavaMailSender getLocalMailSender(Environment environment, SmtpCredential smtpCredential) {
        ensure(smtpCredential, notNullValue());
        ensure(smtpCredential.getEmail(), notNullValue());
        ensure(smtpCredential.getPassword(), notNullValue());

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
        ensure(sender, notNullValue());
        ensure(emailContext, notNullValue());
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        //messageHelper.setFrom();
        messageHelper.setFrom(emailContext.getFrom());
        messageHelper.setTo(emailContext.getTo());
        messageHelper.setSubject(emailContext.getSubject());
        messageHelper.setText(emailContext.getBody(), true);
        if (!CollectionUtils.isEmpty(emailContext.getEmailAttachments()))
            for (EmailAttachmentContext dataSource : emailContext.getEmailAttachments()) {
                messageHelper.addAttachment(dataSource.getOrignalFileName(), dataSource.getDataSource());
            }
        if (!StringUtils.isEmpty(emailContext.getReplyTo()))
            messageHelper.setReplyTo(emailContext.getReplyTo());
        if (!CollectionUtils.isEmpty(emailContext.getCcs()))
            for (String cc : emailContext.getCcs())
                messageHelper.addCc(cc);
        return message;
    }


    public static String getFrom(SmtpCredential smtpCredential, Environment environment) {
        ensure(smtpCredential, notNullValue());
        ensure(environment, notNullValue());
        if (!StringUtils.isEmpty(smtpCredential.getEmail())){
            if(!StringUtils.isEmpty(smtpCredential.getName())){
                return String.format("%s<%s>",smtpCredential.getName(), smtpCredential.getEmail());
            }else
                return smtpCredential.getEmail();
        }
        else
            return PropertyUtil.getProperty(environment, PROPERTY_EMAIL_FROM);
     }


    public static void sendHtmlMail(TaskExecutor taskExecutor, SmtpContext smtpContext) {
        ensure(smtpContext, notNullValue());
        ensure(smtpContext, notNullValue());
        taskExecutor.execute(() -> {
            List<EmailContext> emailContexts = smtpContext.getEmails();
            if (!CollectionUtils.isEmpty(smtpContext.getEmails()))
                for (EmailContext emailContext : emailContexts) {
                    try {
                        MimeMessage message = EmailUtil.convertEmailDtoToMimeMessage(smtpContext.getSender(), emailContext);
                        smtpContext.getSender().send(message);
                    } catch (Exception ex) {
                        log.info("Problem in sending email to {}. message: {} cause: {}", emailContext.getTo(), ex.getMessage(), ex.getCause());
                    }
                }

        });


    }







}
