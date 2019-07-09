package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.controllor.rest.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Contact;
import com.github.ankurpathak.api.domain.model.Password;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.User;
import java.math.BigInteger;
import java.util.Set;

public class DomainConverters {


    public IToDomain<User, BigInteger, UserDto> userDtoCreateToDomain = dto -> User.getInstance()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName())
            .email(Contact.getInstance(dto.getEmail()))
            .roles(Set.of(Role.ROLE_ADMIN));


    public IToDomain<User, BigInteger, UserDto> userDtoRegisterToDomain() {
        return dto -> User.getInstance()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .email(Contact.getInstance(dto.getEmail()))
                .roles(Set.of(Role.ROLE_ADMIN))
                .password(Password.getInstance().value(dto.getPassword()));
    }


}
