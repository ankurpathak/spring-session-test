package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

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
