package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = Model.Mail.MAIL)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mail extends ExtendedDomain<String> implements Serializable {

}
