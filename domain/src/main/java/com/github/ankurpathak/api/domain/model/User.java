package com.github.ankurpathak.api.domain.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.rest.controller.dto.View;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Document(collection = Model.User.USER)
@CompoundIndex(name = Model.User.Index.USER_EMAIL_IDX, sparse = true, unique = true, def = Model.User.Index.Defination.USER_EMAIL_IDX_DEF)
@CompoundIndex(name = Model.User.Index.USER_PHONE_IDX, sparse = true, unique = true, def = Model.User.Index.Defination.USER_PHONE_IDX_DEF)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends ExtendedDomain<BigInteger> implements Serializable {


    private String firstName;
    private String lastName;
    private Contact email;
    @Indexed(name = Model.User.Index.USER_USERNAME_IDX, unique = true, sparse = true)
    private String username;
    private Contact phone;
    private Set<String> roles;
    private boolean enabled;
    private Set<BigInteger> businessIds;


    public Set<BigInteger> getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(Set<BigInteger> businessIds) {
        this.businessIds = businessIds;
    }



    public User password(String password){
        this.password = password;
        return this;
    }


    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




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



    public User addBusinessId(BigInteger businessId) {
        if (businessIds == null)
            businessIds = new HashSet<>();
        if (BigInteger.ZERO.compareTo(businessId) < 0)
            businessIds.add(businessId);
        return this;
    }

    public User removeRole(BigInteger businessId) {
        if (!CollectionUtils.isEmpty(businessIds))
            businessIds.remove(businessId);
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

    public User phone(Contact phone) {
        this.phone = phone;
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
        return Objects.equals(email, user.email) &&
                Objects.equals(username, user.username) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, username, phone);
    }


    public User businessIds(Set<BigInteger> businessIds) {
        this.businessIds = businessIds;
        return this;
    }
}




