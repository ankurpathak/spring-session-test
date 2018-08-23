package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class Email implements Serializable {
    private String value;
    private String tag;
    boolean checked;

    public Email(String value) {
        this(value, null, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        if (checked != email.checked) return false;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }

    public Email(String value, String tag) {
       this(value, tag, false);
    }

    @JsonCreator
    public Email(String value, String tag, boolean checked) {
        this.value = value;
        this.tag = tag;
        this.checked = checked;
    }


    public static Email getInstance(String value){
        return new Email(value);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
