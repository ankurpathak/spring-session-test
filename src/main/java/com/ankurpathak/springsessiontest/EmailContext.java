package com.ankurpathak.springsessiontest;

import java.util.List;

public class EmailContext {
    private final String subject;
    private final String to;
    private final String from;
    private final String body;
    private final List<EmailAttachmentContext> emailAttachments;
    private final List<String> ccs;
    private final String replyTo;

    public EmailContext(String subject, String to, String from, String body, List<EmailAttachmentContext> emailAttachments, List<String> ccs, String replyTo) {
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
        this.emailAttachments = emailAttachments;
        this.ccs = ccs;
        this.replyTo = replyTo;
    }


    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public List<EmailAttachmentContext> getEmailAttachments() {
        return emailAttachments;
    }

    public List<String> getCcs() {
        return ccs;
    }

    public String getReplyTo() {
        return replyTo;
    }
}
