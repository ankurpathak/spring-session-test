package com.ankurpathak.springsessiontest;


import org.springframework.util.CollectionUtils;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User extends Domain<String> implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String password;


    public User addRole(String role){
        if(roles != null)
            roles.add(role);
        return this;
    }

    public User removeRole(String role){
        if(roles != null)
            roles.remove(role);
        return this;
    }


    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public static User getInstance(){
        return new User();
    }


    @Override
    public User id(String id) {
        super.id(id);
        return this;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
