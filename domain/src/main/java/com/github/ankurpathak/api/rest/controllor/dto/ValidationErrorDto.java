package com.github.ankurpathak.api.rest.controller.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrorDto {
    private Map<String, Set<String>> errors = new HashMap<>();


    public static ValidationErrorDto getInstance(){
        return new ValidationErrorDto();
    }


    @JsonAnySetter
    public ValidationErrorDto addError(String path, String message) {
        Set<String> messages = null;
        if(errors.containsKey(path)){
            messages = errors.get(path);

        }else {
            messages = new HashSet<>();
            errors.put(path, messages);
        }
        messages.add(message);
        return this;
    }


    public ValidationErrorDto addErrors(String path,  Set<String> moreMessages) {
        Set<String> messages = null;
        if(errors.containsKey(path)){
            messages = errors.get(path);

        }else {
            messages = new HashSet<>();
            errors.put(path, messages);
        }
        messages.addAll(moreMessages);
        return  this;
    }

    public ValidationErrorDto addErrors(Map<String, Set<String>> errors){
        if(MapUtils.isNotEmpty(errors)){
            this.errors.putAll(errors);
        }
        return this;
    }


    @JsonAnyGetter
    public Map<String, Set<String>> getErrors() {
        return errors;
    }


}
