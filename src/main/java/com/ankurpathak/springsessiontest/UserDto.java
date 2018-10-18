package com.ankurpathak.springsessiontest;

import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;
import com.github.ankurpathak.primitive.bean.constraints.string.Contact;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigInteger;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

@PasswordMatches(groups = {UserDto.Register.class, UserDto.ForgetPassword.class, UserDto.ChangePassword.class})
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

    @NotBlank(groups = {Register.class, ForgetPassword.class, ChangePassword.class})
    @NotContainWhitespace(groups = {Register.class, ForgetPassword.class, ChangePassword.class})
    private String password;
    private String confirmPassword;

    @Contact(groups = Default.class)
    private String contact;


    @NotBlank(groups = ChangePassword.class)
    @NotContainWhitespace(groups = ChangePassword.class)
    private String currentPassword;


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

    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public interface Register {}
    public interface ForgetPassword {}
    public interface ChangePassword {}

}
