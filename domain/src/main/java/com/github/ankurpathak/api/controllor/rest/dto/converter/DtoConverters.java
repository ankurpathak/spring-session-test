package com.github.ankurpathak.api.controllor.rest.dto.converter;

import com.github.ankurpathak.api.controller.rest.dto.converter.IToDto;
import com.github.ankurpathak.api.controllor.rest.dto.UserDto;
import com.github.ankurpathak.api.domain.model.User;

import java.math.BigInteger;

public class DtoConverters {

    public IToDto<User, BigInteger, UserDto> userToUserDto = domain -> UserDto.getInstance()
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .middleName(domain.getMiddleName());

}
