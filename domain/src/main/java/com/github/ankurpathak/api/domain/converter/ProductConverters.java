package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;

public class ProductConverters {
    public static IToDomain<Product, String, ProductDto> createOne = dto -> Product
            .getInstance()
            .name(dto.getName())
            .amount(dto.getAmount())
            .tax(dto.getTax())
            .variable(dto.getVariable())
            .description(dto.getDescription());

}
