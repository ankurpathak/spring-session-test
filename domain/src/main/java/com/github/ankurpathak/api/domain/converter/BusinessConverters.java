package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;

import java.math.BigInteger;

public class BusinessConverters {
    public static IToDomain<Business, BigInteger, BusinessDto> createOne = dto -> Business
            .getInstance()
            .name(dto.getName())
            .addTag(dto.getType());
}
