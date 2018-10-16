package com.ankurpathak.springsessiontest;

import java.math.BigInteger;
import java.util.Set;

public class ToDomainConverters {

    public static IToDomain<User, BigInteger, UserDto> userDtoCreateToDomain = dto -> User.getInstance()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName())
            .email(Contact.getInstance(dto.getEmail()))
            .roles(Set.of(Role.ROLE_ADMIN));


    public static IToDomain<User, BigInteger, UserDto> userDtoRegisterToDomain = dto -> User.getInstance()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName())
            .email(Contact.getInstance(dto.getEmail()))
            .roles(Set.of(Role.ROLE_ADMIN))
            .password(Password.getInstance().value(dto.getPassword()));

}
