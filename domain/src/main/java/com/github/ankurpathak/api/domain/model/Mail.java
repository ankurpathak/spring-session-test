package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = Model.Mail.MAIL)
public class Mail extends ExtendedDomain<String> implements Serializable {

}
