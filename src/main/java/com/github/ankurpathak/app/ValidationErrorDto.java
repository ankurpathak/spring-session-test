package com.github.ankurpathak.app;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrorDto {
    private Map<String, Set<String>> errors = new HashMap<>();


    @JsonAnySetter
    public void addError(String path, String message) {
        Set<String> messages = null;
        if(errors.containsKey(path)){
            messages = errors.get(path);

        }else {
            messages = new HashSet<>();
            errors.put(path, messages);
        }
        messages.add(message);
    }


    public void addErrors(String path,  Set<String> moreMessages) {
        Set<String> messages = null;
        if(errors.containsKey(path)){
            messages = errors.get(path);

        }else {
            messages = new HashSet<>();
            errors.put(path, messages);
        }
        messages.addAll(moreMessages);
    }


    @JsonAnyGetter
    public Map<String, Set<String>> getErrors() {
        return errors;
    }


}
