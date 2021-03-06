package com.github.ankurpathak.api.rest.controllor.dto.converter;

import com.github.ankurpathak.api.rest.controller.dto.converter.IToDto;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.model.User;

import java.math.BigInteger;

public class UserDtoConverters {

    public static IToDto<User, BigInteger, UserDto> userToUserDto = domain -> UserDto.getInstance()
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .middleName(domain.getMiddleName());

}
