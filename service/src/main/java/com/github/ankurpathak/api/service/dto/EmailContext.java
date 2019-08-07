package com.github.ankurpathak.api.service.dto;

import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class EmailContext {
    private final String subject;
    private final String to;
    private final String from;
    private final String body;
    private final boolean html;
    private final List<EmailAttachmentContext> emailAttachments;
    private final List<String> ccs;
    private final List<String> bccs;
    private final String replyTo;

    public EmailContext(String subject, String to, String from, String body, List<EmailAttachmentContext> emailAttachments, List<String> ccs, String replyTo) {
        this(subject, to, from, body, true, emailAttachments, ccs, Collections.emptyList(), replyTo);
    }

    public EmailContext(String subject, String to, String from, String body, List<EmailAttachmentContext> emailAttachments, List<String> ccs, List<String> bccs, String replyTo) {
        this(subject, to, from, body, true, emailAttachments, ccs, bccs, replyTo);
    }


    public EmailContext(String subject, String to, String from, String body, boolean html, List<EmailAttachmentContext> emailAttachments, List<String> ccs, List<String> bccs, String replyTo) {
        require(subject, MatcherUtil.notStringEmpty());
        require(to, MatcherUtil.notStringEmpty());
        require(from, MatcherUtil.notStringEmpty());
        require(body, MatcherUtil.notStringEmpty());
        require(emailAttachments, notNullValue());
        require(ccs, notNullValue());
        require(bccs, notNullValue());
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
        this.html = html;
        this.emailAttachments = emailAttachments;
        this.ccs = ccs;
        this.bccs = bccs;
        this.replyTo = replyTo;
    }


    public Mail toMail() {
        return Mail.getInstance()
                .subject(this.subject)
                .to(this.to)
                .from(this.from)
                .body(this.body)
                .ccs(Sets.newHashSet(this.ccs))
                .bccs(Sets.newHashSet(this.bccs))
                .replyTo(this.replyTo);

    }

    public boolean isHtml() {
        return html;
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
