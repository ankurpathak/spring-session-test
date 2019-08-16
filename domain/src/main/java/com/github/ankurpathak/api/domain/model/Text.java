package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = Model.Text.TEXT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Text extends BusinessExtendedDomain<String> implements Serializable {
    private String body;
    private String text;
    private String userTo;
    private String customerTo;



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getCustomerTo() {
        return customerTo;
    }

    public void setCustomerTo(String customerTo) {
        this.customerTo = customerTo;
    }

    public Text body(String body) {
        this.body = body;
        return this;
    }

    public Text text(String text) {
        this.text = text;
        return this;
    }

    public Text userTo(String userTo) {
        this.userTo = userTo;
        return this;
    }

    public Text customerTo(String customerTo) {
        this.customerTo = customerTo;
        return this;
    }
}
