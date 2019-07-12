package com.github.ankurpathak.api.constant;

public interface Params {
    String ID = "id";
    String EMAIL = "email";
    String TOKEN = "token";
    String TOKEN_ID = "tokenId";
    String VALUE = "value";
    String ASC = "asc";
    String ASC_UPPERCASE = "ASC";
    String DESC = "desc";
    String DESC_UPPERCASE = "DESC";
    String BLOCK = "block";
    String IDS = "ids";
    String RSQL = "rsql";
    String FIELD = "field";
    String COUNT = "count";
    String MONGO_ID = "_id";
    String I ="i";
    String PASSWORD = "password";
    String PATCH = "patch";
    String JSON = "json";
    String USER = "user";
    String USERNAME = "username";
    String PHONE = "contact";
    String SUB = "sub";
    String PROFILE = "profile";
    String ASYNC = "async";
    String TRUE = "true";
    String FALSE = "false";



    interface Query extends Params {

    }

    interface Path extends Params {

    }

    interface Extas extends Params {

    }

    interface Default extends Params {

    }
}
