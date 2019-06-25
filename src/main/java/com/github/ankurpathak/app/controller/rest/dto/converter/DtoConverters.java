package com.github.ankurpathak.app.controller.rest.dto.converter;

import com.github.ankurpathak.app.domain.model.User;
import com.github.ankurpathak.app.controller.rest.dto.UserDto;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class DtoConverters {

    public IToDto<User, BigInteger, UserDto> userToUserDto = domain -> UserDto.getInstance()
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .middleName(domain.getMiddleName());

}
