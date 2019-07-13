package com.github.ankurpathak.api.constant;

public interface Model {

    String USER = "users";
    String ROLE = "roles";
    String TOKEN = "tokens";
    String SEQUENCE = "sequences";


    interface Domain {
        interface Field {
            String ID = "id";
            String VALUE = "value";


        }
    }

    interface User {

        interface Field extends Domain.Field {

            String EMAIL = "email";
            String EMAIL_VALUE = "email.value";
            String EMAIL_TOKEN_ID = "email.tokenId";
            String ENABLED = "enabled";
            String PHONE = "phone";
            String PHONE_VALUE = "phone.value";
        }

        interface Index {

        }

        interface Query {

        }


        interface QueryKey {
            String EMAIL = "email.value";
            String PHONE = "phone.value";
            String PHONE_TOKEN_ID = "phone.tokenId";
            String EMAIL_TOKEN_ID = "email.tokenId";
            String PASSWORD_TOKEN_ID = "password.tokenId";
            String VALUE = "value";
        }



    }


    interface Role {

        interface Field extends Domain.Field {

        }

        interface Index {

        }

        interface Query {

        }

    }


    interface Token {

        interface Field extends Domain.Field{
        }

        interface Index {

        }

        interface Query {

        }



    }


    interface Index {
        String USER_EMAIL_IDX = "usersEmailIdx";
        String ROLE_NAME_IDX = "rolesNameIdx";
        String USER_USERNAME_IDX = "usersUsernameIdx";
        String USER_EMAIL_IDX_DEF = "{ 'email.value' : 1}";
        String USER_PHONE_IDX_DEF = "{ 'phone.value' : 1}";
        String USER_PHONE_IDX = "usersPhoneIdx";
        String TOKEN_EXPIRY_IDX  = "tokensExpiryIdx";

        String TOKEN_VALUE_IDX = "tokensValueIdx";
        String USER_EMAIL_TOKEN_ID_IDX = "usersEmailTokenIdx";
        String USER_EMAIL_TOKEN_ID_IDX_DEF = "{ 'email.tokenId' : 1}";
        String USER_PHONE_TOKEN_ID_IDX = "usersPhoneTokenIdx";
        String USER_PHONE_TOKEN_ID_IDX_DEF = "{ 'phone.tokenId' : 1}";
        String USER_PASSWORD_TOKEN_ID_IDX = "usersPasswordTokenIdx";
        String USER_PASSWORD_TOKEN_ID_IDX_DEF = "{ 'password.tokenId' : 1}";
    }


    interface Field{
        public static final String FIELD_ID = "_id";
        public static final String FIELD_CURRENT = "curr";

    }
}
