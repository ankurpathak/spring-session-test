package com.ankurpathak.springsessiontest;

public interface Documents {

    String USER = "users";
    String ROLE = "roles";
    String TOKEN = "tokens";
    String SEQUENCE = "sequences";


    interface Domain {
        interface Field {
            String ID = "id";
        }
    }

    interface User {

        interface Field extends Domain.Field {

            String EMAIL = "email";
            String EMAIL_TOKEN_ID = "email.tokenId";
        }

        interface Index {

        }

        interface Query {

        }


        interface QueryKey {
            String EMAIL = "email.value";
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
