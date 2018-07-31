package com.ankurpathak.springsessiontest;

import com.github.ankurpathak.bean.constraints.NotContainConsecutivePeriod;
import com.github.ankurpathak.bean.constraints.UsernamePattern;

import java.io.Serializable;

public class UsernameDto implements Serializable {

    @Override
    public String toString() {
        return "UsernameDto{" +
                "username='" + username + '\'' +
                '}';
    }


    @UsernamePattern(includeUnderscore = true)
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
