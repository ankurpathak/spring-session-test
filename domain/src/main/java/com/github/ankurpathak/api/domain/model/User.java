package com.github.ankurpathak.api.domain.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.controller.rest.dto.View;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;


@Document(collection = Model.USER)
@CompoundIndexes({
        @CompoundIndex(name = Model.Index.USER_EMAIL_IDX, sparse = true, unique = true, def = Model.Index.USER_EMAIL_IDX_DEF),
        @CompoundIndex(name = Model.Index.USER_CONTACT_IDX, sparse = true, unique = true, def = Model.Index.USER_CONTACT_IDX_DEF),
        @CompoundIndex(name = Model.Index.USER_EMAIL_TOKEN_ID_IDX, sparse = true, unique = true, def = Model.Index.USER_EMAIL_TOKEN_ID_IDX_DEF)
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends ExtendedDomain<BigInteger> implements Serializable {


    private String firstName;
    private String lastName;
    private Contact email;
    @Indexed(name = Model.Index.USER_USERNAME_IDX, unique = true, sparse = true)
    private String username;
    private Contact phone;
    private Set<String> roles;
    private boolean enabled;





    public User password(Password password){
        this.password = password;
        return this;
    }


    private Password password;


    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    private Set<Contact> emails;

    private Set<Contact> contacts;


    public User addRole(String role) {
        if (roles == null)
            roles = new HashSet<>();
        if (!StringUtils.isEmpty(role))
            roles.add(role);
        return this;
    }

    public User removeRole(String role) {
        if (!CollectionUtils.isEmpty(roles))
            roles.remove(role);
        return this;
    }


    public User addEmail(Contact email) {
        if (emails == null)
            emails = new HashSet<>();
        if (email != null)
            emails.add(email);
        return this;
    }

    public User removeEmail(Contact email) {
        if (!CollectionUtils.isEmpty(emails))
            roles.remove(email);
        return this;
    }

    public User addContact(Contact contact) {
        if (contacts == null)
            contacts = new HashSet<>();
        if (contact != null)
            contacts.add(contact);
        return this;
    }

    public User removeContact(Contact contact) {
        if (!CollectionUtils.isEmpty(contacts))
            roles.remove(contact);
        return this;
    }

    private String middleName;

    @JsonView({View.Public.class, View.Me.class})
    public String getMiddleName() {
        return middleName;
    }




    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User email(Contact email) {
        this.email = email;
        return this;
    }

    public User roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }


    public boolean isEnabled() {
        return enabled;
    }



    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected User() {
        enabled = false;

    }

    @JsonCreator
    public static User getInstance() {
        return new User();
    }


    @Override
    public User id(BigInteger id) {
        super.id(id);
        return this;
    }

    @JsonView({View.Public.class, View.Me.class})
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonView({View.Public.class, View.Me.class})
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonView({View.Me.class})
    public Contact getEmail() {
        return email;
    }

    public void setEmail(Contact email) {
        this.email = email;
    }

    @JsonView({View.Public.class, View.Me.class})
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    @Override
    @JsonView({View.Public.class, View.Me.class})
    public BigInteger getId() {
        return super.getId();
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User contact(Contact phone) {
        this.phone = phone;
        return this;
    }

    public User emails(Set<Contact> emails) {
        this.emails = emails;
        return this;
    }

    public User contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public User middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public User enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonView({View.Me.class})
    public Contact getPhone() {
        return phone;
    }

    public void setPhone(Contact phone) {
        this.phone = phone;
    }
    @JsonView({View.Me.class})
    public Set<Contact> getEmails() {
        return emails;
    }

    public void setEmails(Set<Contact> emails) {
        this.emails = emails;
    }

    @JsonView({View.Me.class})
    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public static final String ANONYMOUS_USERNAME= "anonymous";
    public static final User ANONYMOUS_USER;
    public static final BigInteger ANONYMOUS_ID = BigInteger.ONE;
    static
    {
        ANONYMOUS_USER = getInstance().username(ANONYMOUS_USERNAME)
                .roles(Set.of(Role.ROLE_ANONYMOUS))
                .id(ANONYMOUS_ID)
                .enabled(false);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }



}




