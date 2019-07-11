package com.github.ankurpathak.api.service.dto;

import org.springframework.mail.javamail.JavaMailSender;

import java.io.Serializable;
import java.util.List;

public class SmtpContext implements Serializable {
    private final List<EmailContext> emails;
    private final JavaMailSender sender;
    private final boolean async;

    public List<EmailContext> getEmails() {
        return emails;
    }

    public SmtpContext(List<EmailContext> emails, JavaMailSender sender, boolean async) {
        this.emails = emails;
        this.sender = sender;
        this.async = async;
    }


    public SmtpContext(List<EmailContext> emails, JavaMailSender sender){
        this(emails, sender, true);
    }

    public JavaMailSender getSender() {
        return sender;
    }


    public boolean isAsync() {
        return async;
    }
}
