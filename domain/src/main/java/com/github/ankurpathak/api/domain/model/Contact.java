package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.rest.controller.dto.View;
import com.github.ankurpathak.api.util.MatcherUtil;

import java.io.Serializable;
import java.util.Objects;

import static org.valid4j.Assertive.require;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Contact implements Serializable {

    public static final String TAG_PRIMARY = "primary";
    public static final String TAG_ADDED = "added";

    @JsonView({View.Me.class})
    private String value;
    private String tag;
    private boolean checked;


    public static Contact getInstance(String value){
        require(value, MatcherUtil.notStringEmpty());
        return new Contact(value);
    }




    public Contact(String value) {
        this(value, TAG_PRIMARY, false);
    }

    public static Contact getInstance(String value, String tag) {
        return  new Contact(value, tag, false);
    }




    public Contact(String value, String tag) {
        this(value, tag, false);
    }


    public Contact(String value, String tag, boolean checked) {
        this.value = value;
        this.tag = tag;
        this.checked = checked;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Contact() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return value.equals(contact.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Contact checked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public Contact tag(String tag) {
        this.tag = tag;
        return this;
    }
}
