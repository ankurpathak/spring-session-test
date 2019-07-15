package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.rest.controller.dto.View;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {

    public static final String TAG_PRIMARY = "primary";

    @JsonView({View.Me.class})
    private String value;
    private String tag;
    private boolean checked;

    public static Contact getInstance(String value){
        return new Contact(value);
    }


    public Contact(String value) {
        this(value, null, false);
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
}
