package com.ankurpathak.springsessiontest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class UpdateDomainUpdaters {


    private final PasswordEncoder passwordEncoder;


    public IUpdateDomain<User, BigInteger, UserDto> forgetPasswordUpdater() {
        return (user, dto) -> User.getInstance()
                .password(Password.getInstance().value(passwordEncoder.encode(dto.getPassword())));
    }


    public UpdateDomainUpdaters(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
