package com.github.ankurpathak.api.constant;

import java.nio.file.Paths;

public interface ApiPaths {
    String PATH_API = "/api";
    String PATH_ME = "/me";
    String PATH_ERROR = "/error";

    String PATH_ACCOUNT = "/account";
    String PATH_SEARCH_BY_FIEND_USER = "/users/search/{field}/{value}";
    String PATH_SEARCH_USER = "/users/search";
    String PATH_LIST_FIELD_USER = "/users/list/{field}/{value}";
    String PATH_ACCOUNT_EMAIL = "/account/email/{email}";
    String PATH_ACCOUNT_ENABLE = "/account/token/{token}";
    String PATH_FORGET_PASSWORD_EMAIL = "/password/email/{email}";
    String PATH_FORGET_PASSWORD = "/password";
    String PATH_FORGET_PASSWORD_ENABLE = "/password/token/{token}";
    String PATH_CHANGE_PASSWORD = "/password";
    String PATH_USER = "/user";
    String PATH_LOGOUT = "/logout";
    String PATH_FAVICON = "/favicon.ico";
    String PATH_REMEMBER_ME = "/remember-me";
    String PATH_LOGIN = "login";
    String PATH_LOGIN_OTP = "/login/{token}";
    String PATH_BUSINESS = "/business";
    String PATH_BANK_STATE = "/bank/state";
    String PATH_BANK_DISTRICT = "/bank/state/district";
    String PATH_BANK_BRANCH = "/bank/state/district/branch";
    String PATH_BANK_IFSC = "/bank/{bank}";
    String PATH_BANK = "/bank";
    String PATH_STATE = "/state";
    String PATH_PIN_CODE = "/state/{pinCode}";
    String PATH_STATE_DISTRICT = "/state/district";
    String PATH_STATE_PIN_CODE = "/state/district/pin-code";
    String PATH_DISTRICT= "/district";
    String PATH_SERVICE = "/service";
    static String apiPath(String path){
        return Paths.get(PATH_API, path).toString();
    }

}
