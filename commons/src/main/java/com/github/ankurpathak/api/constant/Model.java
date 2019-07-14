package com.github.ankurpathak.api.constant;

public interface Model {
    interface Domain {
        interface Field {
            String ID = "_id";
        }

        interface Index {

            interface Defination {

            }
        }

        interface Query { }
    }

    interface User {

        String USER = "users";

        interface Field extends Domain.Field {
            String ENABLED = "enabled";
            String EMAIL = "email";
            String EMAIL_VALUE = "email.value";
            String PHONE = "phone";
            String PHONE_VALUE = "phone.value";

        }

        interface Index {
            String USER_EMAIL_IDX = "usersEmailIdx";
            String ROLE_NAME_IDX = "rolesNameIdx";
            String USER_USERNAME_IDX = "usersUsernameIdx";
            String USER_PHONE_IDX = "usersPhoneIdx";


            interface Defination extends Domain.Index.Defination{
                String USER_EMAIL_IDX_DEF = "{ 'email.value' : 1}";
                String USER_PHONE_IDX_DEF = "{ 'phone.value' : 1}";
            }
        }

        interface Query { }
    }


    interface Role {

        String ROLE = "roles";

        interface Field extends Domain.Field { }

        interface Index extends Domain.Index{
            String ROLE_NAME_IDX = "rolesNameIdx";

            interface Defination extends Domain.Index.Defination{ }
        }

        interface Query extends Domain.Query{ }

    }


    interface Token {
        String TOKEN = "tokens";

        interface Field extends Domain.Field{
            String VALUE = "value";
        }

        interface Index extends Domain.Index{
            String TOKEN_EXPIRY_IDX  = "tokensExpiryIdx";

            interface Defination extends Domain.Index.Defination{ }
        }

        interface Query extends Domain.Query{ }

    }

    interface Sequence {
        String SEQUENCE = "sequences";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index extends Domain.Index{
            interface Defination extends Domain.Index.Defination{ }
        }

        interface Query extends Domain.Query{ }


    }

}
