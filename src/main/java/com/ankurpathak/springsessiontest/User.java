package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Document(collection = DocumentCollections.USERS)
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

    protected User(){

    }
    @JsonCreator
    public static User getInstance(){
        return new User();
    }


    @Override
    public User id(String id) {
        super.id(id);
        return this;
    }

    @JsonView(View.Public.class)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonView(View.Public.class)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonView(View.Public.class)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonView(View.Public.class)
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


    @Override
    @JsonView(View.Public.class)
    public String getId() {
        return super.getId();
    }

    public interface View {

        interface Public {

        }
    }
}
