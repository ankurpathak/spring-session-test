package com.github.ankurpathak.app;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.ensure;

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

    //Property

    //Templates
    public static final String TEMPLATE_ACCOUNT_ENABLE ="account-enable";
    public static final String TEMPLATE_FORGET_PASSWORD="forget-password";
    //Templates






    @Override
    public String createAccountEnableHtml(User user, Token token) {
        return createTokenMail(user, token, PROPERTY_UI_ACCOUNT_ENABLE_URL, TEMPLATE_ACCOUNT_ENABLE);
    }

    @Override
    public String createForgetPasswordHtml(User user, Token token) {
        return createTokenMail(user, token, PROPERTY_UI_FORGET_PASSWORD_URL, TEMPLATE_FORGET_PASSWORD);
    }

    private String createTokenMail(User user, Token token, String url, String template){
        ensure(user, notNullValue());
        ensure(token, notNullValue());
        Context context = new Context();
        context.setVariable(MODEL_PARAM_TOKEN, token.getValue());
        context.setVariable(MODEL_PARAM_LINK, PropertyUtil.getProperty(environment, url));
        return templateEngine.process(template, context);
    }



}
