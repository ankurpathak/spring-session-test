package com.ankurpathak.springsessiontest;

public interface DocumentCollections {

    String USER = "users";
    String ROLE = "roles";
    String TOKEN = "tokens";
    String SEQUENCE = "sequences";


    interface Index {
        String USER_EMAIL_IDX = "usersEmailIdx";
        String ROLE_NAME_IDX = "rolesNameIdx";
        String USER_USERNAME_IDX = "usersUsernameIdx";
        String USER_EMAIL_IDX_DEF = "{ 'email.value' : 1}";
        String USER_CONTACT_IDX_DEF = "{ 'contact.value' : 1}";
        String USER_CONTACT_IDX = "usersContactIdx";
        String TOKEN_EXPIRY_IDX  = "tokensExpiryIdx";

        String TOKEN_VALUE_IDX = "tokensValueIdx";
        String USER_EMAIL_TOKEN_ID_IDX = "usersEmailTokenIdx";
        String USER_EMAIL_TOKEN_ID_IDX_DEF = "{ 'email.tokenId' : 1}";
    }


    interface Field{
        public static final String FIELD_ID = "_id";
        public static final String FIELD_CURRENT = "curr";

    }
}
