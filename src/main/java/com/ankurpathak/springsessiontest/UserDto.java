package com.ankurpathak.springsessiontest;

import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

@PasswordMatches(groups = {DomainDto.Register.class, DomainDto.PasswordReset.class})
public class UserDto extends DomainDto<User, BigInteger> implements Serializable {

    @NotBlank(groups = Default.class)
    @NotContainWhitespace(groups = Default.class)
    private String firstName;
    @NotContainWhitespace(groups = Default.class)
    @NotBlank(groups = Default.class)
    private String lastName;
    @NotBlank(groups = Default.class)
    @com.github.ankurpathak.primitive.bean.constraints.string.Email(groups = {Register.class})
    private String email;
    private String username;
    @NotContainWhitespace(groups = Default.class)
    private String middleName;

    @NotBlank(groups = {Register.class, PasswordReset.class})
    private String password;
    private String confirmPassword;
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
    public User toDomain(Class<?> type) {
        if (type != null) {
            if (type.equals(Register.class)) {
                return forRegister();
            }
        }
        return forCreate();
    }


    private User forCreate() {
        return User.getInstance()
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .email(Contact.getInstance(email))
                .roles(Set.of(Role.ROLE_USER));
    }

    private User forRegister() {
        return User.getInstance()
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .email(Contact.getInstance(email))
                .roles(Set.of(Role.ROLE_ADMIN))
                .password(Password.getInstance().value(password));
    }


    private User forForgetPassword(User user){
        user.setPassword(user.getPassword().value(this.password));
        return user;
    }

    @Override
    public User updateDomain(User user, Class<?> type) {
        ensure(user, notNullValue());
        ensure(type, notNullValue());
        if (type.equals(PasswordReset.class)) {
            return forForgetPassword(user);
        }


        return user;
    }


}
