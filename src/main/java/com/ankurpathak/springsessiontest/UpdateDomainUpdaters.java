package com.ankurpathak.springsessiontest;

import java.math.BigInteger;

public class UpdateDomainUpdaters {

    public static IUpdateDomain<User, BigInteger, UserDto> forgetPasswordUpdater = (user, dto) -> User.getInstance()
            .password(Password.getInstance().value(dto.getPassword()));
}
