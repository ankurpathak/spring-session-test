package com.github.ankurpathak.api.rsql;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ArrayUtils;

import java.time.Instant;
import java.util.*;

public class RSQLFieldMapper {


    private static Object castFilterField(String field, String value) {
        if(List.of(Model.Domain.Field.CREATED, Model.Domain.Field.UPDATED).contains(field)){
            try{
                return Instant.parse(value);
            }catch (RuntimeException ex){
                throw new InvalidException(ApiCode.INVALID_RSQL_OPERAND, field, value);
            }
        }
        return value;
    }


    public static List<?> castFilterField(String resource, String field, List<String> arguments) {
        List<Object> castedArguments = new ArrayList<>();
        if(CollectionUtils.isEmpty(arguments))
            return arguments;
        else{
            arguments.forEach(argument -> castedArguments.add(castFilterField(field, argument)));
            return castedArguments;
        }
    }

}



