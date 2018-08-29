package com.ankurpathak.springsessiontest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.expression.Sets;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Document(collection = DocumentCollections.USERS)
@CompoundIndexes({
        @CompoundIndex(name = DocumentCollections.Index.USERS_EMAIL_IDX, sparse = true, unique = true, def = DocumentCollections.Index.USERS_EMAIL_IDX_DEF),
        @CompoundIndex(name = DocumentCollections.Index.USERS_CONTACT_IDX, sparse = true, unique = true, def = DocumentCollections.Index.USERS_CONTACT_IDX_DEF)
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends Domain<BigInteger> implements Serializable {


    private String firstName;
    private String lastName;
    private Contact email;
    @Indexed(name = DocumentCollections.Index.USERS_USERNAME_IDX, unique = true, sparse = true)
    private String username;
    private Contact contact;
    private Set<String> roles;
    private String password;


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

    public User password(String password) {
        this.password = password;
        return this;
    }


    protected User() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public User contact(Contact contact) {
        this.contact = contact;
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

    public interface View {

        interface Public {

        }

        public class Me {
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonView({View.Me.class})
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
    public static final User ANONYMOUS_USER = User.getInstance();
    public static final BigInteger ANONYMOUS_ID = BigInteger.ONE;
    static
    {
        ANONYMOUS_USER.username(ANONYMOUS_USERNAME)
                .roles(Set.of(Role.ROLE_ANONYMOUS))
                .id(ANONYMOUS_ID);
    }



}




