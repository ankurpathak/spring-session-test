package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class Contact implements Serializable {
    private String value;
    private String tag;
    boolean checked;


    public Contact(String value) {
        this(value, null, false);
    }


    public Contact(String value, String tag) {
        this(value, tag, false);
    }

    @JsonCreator
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (checked != contact.checked) return false;
        return value.equals(contact.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
