package com.ankurpathak.springsessiontest;

import java.io.Serializable;
import java.util.Set;

public class UserDto extends DomainDto<String> implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String middleName;
    private String password;
    private String matchingPassword;


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Domain<String> toDomain() {
        return User.getInstance()
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .password(password)
                .email(Email.getInstance(email)).roles(Set.of(Role.ROLE_USER));
    }

    @Override
    public Domain<String> updateDomain(Domain<String> domain) {
        if(domain instanceof User){
            User user = (User) domain;
            if(firstName != null) user.firstName(firstName);
            if(lastName != null) user.lastName(lastName);
            if(middleName != null) user.middleName(middleName);
        }
        return domain;
    }
}
