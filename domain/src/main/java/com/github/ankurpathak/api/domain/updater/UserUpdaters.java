package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;

import java.math.BigInteger;

public class UserUpdaters {




    public static IUpdateDomain<User, BigInteger, UserDto> forgetPasswordUpdater() {
        return (user, dto) -> user
                .password(dto.getEncodedPassword());
    }


    public static IUpdateDomain<User,BigInteger,UserDto> profileUpdater = (user, dto) -> user
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .middleName(dto.getMiddleName());


}
