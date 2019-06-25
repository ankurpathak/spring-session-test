package com.github.ankurpathak.app.service.dto;

import com.github.ankurpathak.app.service.dto.EmailContext;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.Serializable;
import java.util.List;

public class SmtpContext implements Serializable {
    private List<EmailContext> emails;
    private JavaMailSender sender;

    public List<EmailContext> getEmails() {
        return emails;
    }

    public SmtpContext(List<EmailContext> emails, JavaMailSender sender) {
        this.emails = emails;
        this.sender = sender;
    }

    public void setEmails(List<EmailContext> emails) {
        this.emails = emails;
    }

    public JavaMailSender getSender() {
        return sender;
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }
}
