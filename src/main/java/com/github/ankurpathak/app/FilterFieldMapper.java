package com.github.ankurpathak.app;

import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

public class FilterFieldMapper {

    private static final Map<String, FilterField> map = new HashMap<>();


    static {
        map.put("users/id", new FilterField(String.class.getSimpleName()));
        map.put("users/firstName", new FilterField(String.class.getSimpleName()));
        map.put("users/lastName", new FilterField(String.class.getSimpleName()));
        map.put("users/created", new FilterField(Instant.class.getSimpleName()));
        map.put("users/updated", new FilterField(Instant.class.getSimpleName()));
    }


    private static Object castFilterField(FilterField filterField, String value) {
        if (Objects.equals(filterField.getDataType(), boolean.class.getSimpleName()) ||
            Objects.equals(filterField.getDataType(), Boolean.class.getSimpleName())){
            return Boolean.valueOf(value);
        }else if (Objects.equals(filterField.getDataType(), long.class.getSimpleName()) ||
                Objects.equals(filterField.getDataType(), Long.class.getSimpleName())) {
            return PrimitiveUtils.toLong(value);
        }else if (Objects.equals(filterField.getDataType(), Integer.class.getSimpleName()) ||
                Objects.equals(filterField.getDataType(), Integer.class.getSimpleName())) {
            return PrimitiveUtils.toInteger(value);
        } else if (Objects.equals(filterField.getDataType(), Instant.class.getSimpleName())) {
            return PrimitiveUtils.toDate(value);
        }else if (Objects.equals(filterField.getDataType(), Date.class.getSimpleName())) {
            return PrimitiveUtils.toDate(value);
        }else {
            return value;
        }
    }


    public static List<?> castFilterField(String resource, String field, List<String> arguments) {
        List<Object> castedArguments = new ArrayList<>();
        String filterFieldKey = String.format("%s/%s", resource, field);
        FilterField filterField = map.get(filterFieldKey);
        if(filterField == null || CollectionUtils.isEmpty(arguments))
            return arguments;
        else{
            arguments.forEach(argument -> castedArguments.add(castFilterField(filterField, argument)));
            return castedArguments;
        }
    }

}



