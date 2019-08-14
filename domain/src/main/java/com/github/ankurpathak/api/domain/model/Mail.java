package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Document(collection = Model.Mail.MAIL)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mail extends BusinessExtendedDomain<String> implements Serializable {
    private String subject;
    private String body;
    private String from;
    private String to;
    private String replyTo;
    private Set<String> ccs;
    private Set<String> bccs;
    private Set<String> attachmentTds;
    private BigInteger toUserId;
    private CustomerId toCustomerId;

    public BigInteger getToUserId() {
        return toUserId;
    }

    public void setToUserId(BigInteger toUserId) {
        this.toUserId = toUserId;
    }

    public CustomerId getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(CustomerId toCustomerId) {
        this.toCustomerId = toCustomerId;
    }

    public static Mail getInstance(){
        return new Mail();
    }

    public Mail addCc(String cc) {
        if (this.ccs == null)
            this.ccs = new HashSet<>();
        if (!StringUtils.isEmpty(cc))
            this.ccs.add(cc);
        return this;
    }

    public Mail removeCc(String cc) {
        if (!CollectionUtils.isEmpty(this.ccs))
            if (!StringUtils.isEmpty(cc))
                this.ccs.remove(cc);
        return this;
    }

    public Mail addBcc(String bcc) {
        if (this.bccs == null)
            this.bccs = new HashSet<>();
        if (!StringUtils.isEmpty(bcc))
            this.bccs.add(bcc);
        return this;
    }

    public Mail removeBcc(String bcc) {
        if (!CollectionUtils.isEmpty(this.bccs))
            if (!StringUtils.isEmpty(bcc))
                this.bccs.remove(bcc);
        return this;
    }

    public Mail addAttachment(String attachmentId) {
        if (this.attachmentTds == null)
            this.attachmentTds = new LinkedHashSet<>();
        if (!StringUtils.isEmpty(attachmentId))
            this.attachmentTds.add(attachmentId);
        return this;
    }

    public Mail removeAttachment(String attachmentId) {
        if (!CollectionUtils.isEmpty(this.attachmentTds))
            if (!StringUtils.isEmpty(attachmentId))
                this.attachmentTds.remove(attachmentId);
        return this;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Set<String> getCcs() {
        return ccs;
    }

    public void setCcs(Set<String> ccs) {
        this.ccs = ccs;
    }

    public Set<String> getBccs() {
        return bccs;
    }

    public void setBccs(Set<String> bccs) {
        this.bccs = bccs;
    }

    public Set<String> getAttachmentTds() {
        return attachmentTds;
    }

    public void setAttachmentTds(Set<String> attachmentTds) {
        this.attachmentTds = attachmentTds;
    }

    public Mail subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail body(String body) {
        this.body = body;
        return this;
    }

    public Mail from(String from) {
        this.from = from;
        return this;
    }


    public Mail to(String to) {
        this.to = to;
        return this;
    }

    public Mail replyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public Mail ccs(Set<String> ccs) {
        this.ccs = ccs;
        return this;
    }

    public Mail bccs(Set<String> bccs) {
        this.bccs = bccs;
        return this;
    }

    public Mail attachmentTds(Set<String> attachmentTds) {
        this.attachmentTds = attachmentTds;
        return this;
    }

    public Mail toUserId(BigInteger userId) {
        this.toUserId = userId;
        return this;
    }

    public Mail toCustomerId(CustomerId customerId) {
        this.toCustomerId = customerId;
        return this;
    }
}
