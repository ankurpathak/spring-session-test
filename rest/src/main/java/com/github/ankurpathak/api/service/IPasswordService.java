package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;

import java.math.BigInteger;

public interface IPasswordService  {

    Token.TokenStatus forgetPasswordEnable(String token);

    void validateExistingPassword(User user, UserDto dto);

    void forgotPasswordEmail(String email, boolean async);
}
