package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.controllor.rest.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Password;
import com.github.ankurpathak.api.domain.model.User;

import java.math.BigInteger;

public class DomainUpdaters {




    public IUpdateDomain<User, BigInteger, UserDto> forgetPasswordUpdater() {
        return (user, dto) -> user
                .password(Password.getInstance().value(dto.getPassword()));
    }


    public IUpdateDomain<User,BigInteger,UserDto> profileUpdater = (user, dto) -> user
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName());


}
