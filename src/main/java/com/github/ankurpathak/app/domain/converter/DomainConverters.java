package com.github.ankurpathak.app.domain.converter;

import com.github.ankurpathak.app.controller.rest.dto.UserDto;
import com.github.ankurpathak.app.domain.model.Contact;
import com.github.ankurpathak.app.domain.model.Password;
import com.github.ankurpathak.app.domain.model.Role;
import com.github.ankurpathak.app.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Set;

@Component
public class DomainConverters {


    private final PasswordEncoder passwordEncoder;

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
                .password(Password.getInstance().value(this.passwordEncoder.encode(dto.getPassword())));
    }



    public DomainConverters(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
