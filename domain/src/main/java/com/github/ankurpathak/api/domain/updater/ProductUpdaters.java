package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import com.google.common.collect.Sets;

public class ProductUpdaters {
    public static IUpdateDomain<Product, String, ProductDto> updateProduct() {
        return (product, dto) -> product
                .name(dto.getName())
                .amount(dto.getAmount())
                .tax(dto.getTax())
                .variable(dto.getVariable())
                .description(dto.getDescription())
                .images(Sets.newLinkedHashSet(dto.getImages()));
    }
}
