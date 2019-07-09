package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;
import com.github.ankurpathak.primitive.bean.constraints.string.Contact;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigInteger;

@PasswordMatches(groups = {UserDto.Register.class, UserDto.ForgetPassword.class, UserDto.ChangePassword.class})
public class UserDto extends DomainDto<User, BigInteger> implements Serializable {

    @NotBlank(groups = Default.class)
    @NotContainWhitespace(groups = Default.class)
    private String firstName;
    @NotContainWhitespace(groups = Default.class)
    @NotBlank(groups = Default.class)
    private String lastName;
    @NotBlank(groups = Register.class)
    @com.github.ankurpathak.primitive.bean.constraints.string.Email(groups = {Register.class})
    private String email;
    private String username;
    @NotContainWhitespace(groups = Default.class)
    private String middleName;

    @NotBlank(groups = {Register.class, ForgetPassword.class, ChangePassword.class})
    @NotContainWhitespace(groups = {Register.class, ForgetPassword.class, ChangePassword.class})
    private String password;
    private String confirmPassword;

    @Contact(groups = Register.class)
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

    public UserDto firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDto lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDto email(String email) {
        this.email = email;
        return this;
    }

    public UserDto username(String username) {
        this.username = username;
        return this;
    }

    public UserDto middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public UserDto password(String password) {
        this.password = password;
        return this;
    }

    public UserDto confirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public UserDto contact(String contact) {
        this.contact = contact;
        return this;
    }

    public UserDto currentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }


    public interface Register {}
    public interface ForgetPassword {}
    public interface ChangePassword {}


    public static UserDto getInstance(){
        return new UserDto();
    }



}
