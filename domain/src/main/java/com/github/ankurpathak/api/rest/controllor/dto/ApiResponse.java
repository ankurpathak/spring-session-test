package com.github.ankurpathak.api.rest.controller.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.require;

public class ApiResponse {

    private Map<String, Object> extras = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getExtras() {
        return extras;
    }


    @JsonAnySetter
    public ApiResponse addExtra(String name, Object value) {
        if(this.extras != null)
            this.extras.put(name, value);
        return this;



    }


    public ApiResponse addExtras(Map<String, Object> extras) {
        if(this.extras != null)
            this.extras.putAll(extras);
        return this;
    }


    private ApiResponse(ApiCode code, String... messages){
        require(code, notNullValue(ApiCode.class));
        require(messages, not(emptyArray()));
        this.extras.put("code", code.getCode());
        if(ArrayUtils.isNotEmpty(messages)){
            if(messages.length > 1){
                this.extras.put("messages", messages);
            }else {
                this.extras.put("message", messages[0]);
            }
        }

    }



    public static ApiResponse getInstance(ApiCode code, String... messages){
        return new ApiResponse(code, messages);
    }


    public ApiResponse() {
    }
}
