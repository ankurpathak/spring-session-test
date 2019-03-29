package com.github.ankurpathak.app.domain.updater;

import com.github.ankurpathak.app.Password;
import com.github.ankurpathak.app.User;
import com.github.ankurpathak.app.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class DomainUpdaters {


    private final PasswordEncoder passwordEncoder;


    public IUpdateDomain<User, BigInteger, UserDto> forgetPasswordUpdater() {
        return (user, dto) -> user
                .password(Password.getInstance().value(passwordEncoder.encode(dto.getPassword())));
    }


    public IUpdateDomain<User,BigInteger,UserDto> profileUpdater = (user, dto) -> user
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName());


    public DomainUpdaters(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
