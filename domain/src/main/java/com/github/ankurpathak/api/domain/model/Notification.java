package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = Model.Text.TEXT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Notification extends ExtendedDomain<String> implements Serializable {
    //private String ;
    private String actorId;
    private String entity;
    private String entityId;
    private String action;
    private List<String> notifierId;
}
