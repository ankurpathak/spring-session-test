package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.IEmailTemplateService;
import com.github.ankurpathak.api.util.PropertyUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

@Service
public class ThymeleafEmailTemplateService implements IEmailTemplateService {

    //Autowiring
    private final TemplateEngine templateEngine;
    private final Environment environment;

    public ThymeleafEmailTemplateService(TemplateEngine templateEngine, Environment environment) {
        this.templateEngine = templateEngine;
        this.environment = environment;
    }
    //Autowiring

    //Params
    public static final String MODEL_PARAM_TOKEN="token";
    public static final String MODEL_PARAM_LINK = "link";
    //Params


    //Property
    public static final String PROPERTY_UI_ACCOUNT_ENABLE_URL = "ui.account-enable.url";
    public static final String PROPERTY_UI_FORGET_PASSWORD_URL = "ui.forget-password.url";
    public static final String PROPERTY_UI_BUSINESS_ADDED_URL = "ui.business-added.url";

    //Property

    //Templates
    public static final String TEMPLATE_ACCOUNT_ENABLE ="account-enable";
    public static final String TEMPLATE_FORGET_PASSWORD="forget-password";
    public static final String TEMPLATE_BUSINESS_ADDED="business-added";
    //Templates






    @Override
    public String createAccountEnableHtml(User user, Token token) {
        return createUserTokenMail(user, token, PROPERTY_UI_ACCOUNT_ENABLE_URL, TEMPLATE_ACCOUNT_ENABLE);
    }



    @Override
    public String createForgetPasswordHtml(User user, Token token) {
        return createUserTokenMail(user, token, PROPERTY_UI_FORGET_PASSWORD_URL, TEMPLATE_FORGET_PASSWORD);
    }

    @Override
    public String createBusinessAddedHtml(User user, Business business) {
        require(user, notNullValue());
        require(business, notNullValue());
        Context context = new Context();

        return templateEngine.process(TEMPLATE_BUSINESS_ADDED, context);
    }

    private String createUserTokenMail(User user, Token token, String url, String template){
        require(user, notNullValue());
        require(token, notNullValue());
        return createTokenHtml(token, url, template);
    }


    private String createBusinessTokenMail(Business business, Token token, String url, String template){
        require(business, notNullValue());
        require(token, notNullValue());
        return createTokenHtml(token, url, template);
    }


    private String createTokenHtml(Token token, String url, String template){
        Context context = new Context();
        context.setVariable(MODEL_PARAM_TOKEN, token.getValue());
        context.setVariable(MODEL_PARAM_LINK, PropertyUtil.getProperty(environment, url));
        return templateEngine.process(template, context);
    }



}
