package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = Model.Text.TEXT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Text extends ExtendedDomain<String> implements Serializable {

}
