package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.domain.model.Contact;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;

import java.math.BigInteger;
import java.util.Collections;

public class UserConverters {


    public static IToDomain<User, BigInteger, UserDto> userDtoCreateToDomain = dto -> User.getInstance()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName())
            .email(Contact.getInstance(dto.getEmail()))
            .roles(Collections.singleton(Role.ROLE_ADMIN));


    public static IToDomain<User, BigInteger, UserDto> userDtoRegisterToDomain() {
        return dto -> User.getInstance()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .email(Contact.getInstance(dto.getEmail()))
                .roles(Collections.singleton(Role.ROLE_ADMIN))
                .password(dto.getEncodedPassword());
    }


}
