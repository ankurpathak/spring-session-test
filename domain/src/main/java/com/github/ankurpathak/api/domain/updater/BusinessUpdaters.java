package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;

import java.math.BigInteger;

public class BusinessUpdaters {
    public static IUpdateDomain<Business, BigInteger, BusinessDto> updateBusiness = (t, dto) -> t.name(dto.getName());
}
