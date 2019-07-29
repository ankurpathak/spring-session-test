package com.github.ankurpathak.api.constant;

public interface Model {
    interface Domain {
        interface Field {
            String ID = "_id";
            String CANDIDATE_ID = "candidateKey";
        }

        interface Index {

            interface Definition {

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
            String USERNAME = "username";

        }

        interface Index {
            String EMAIL_IDX = "usersEmailIdx";
            String USERNAME_IDX = "usersUsernameIdx";
            String PHONE_IDX = "usersPhoneIdx";


            interface Definition extends Domain.Index.Definition {
                String EMAIL_IDX_DEF = "{ 'email.value' : 1}";
                String PHONE_IDX_DEF = "{ 'phone.value' : 1}";
            }
        }

        interface Query { }
    }


    interface Role {

        String ROLE = "roles";

        interface Field extends Domain.Field { }

        interface Index extends Domain.Index{
            String ROLE_NAME_IDX = "rolesNameIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }


    interface Token {
        String TOKEN = "tokens";

        interface Field extends Domain.Field{
            String VALUE = "value";
        }

        interface Index extends Domain.Index{
            String EXPIRY_IDX = "tokensExpiryIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Sequence {
        String SEQUENCE = "sequences";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }


    }

    interface Business {
        String BUSINESS = "businesses";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Mail {
        String MAIL = "mails";

        interface Field extends Domain.Field{
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }


    interface Product {
        String PRODUCT = "products";

        interface Field extends Domain.Field{
            String NAME  = "name";
        }

        interface Index extends Domain.Index{
            String NAME_IDX = "productsNameIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Text {
        String TEXT = "texts";

        interface Field extends Domain.Field{
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Customer {
        String CUSTOMER = "customers";

        interface Field extends Domain.Field{
            String NAME  = "name";
        }

        interface Index extends Domain.Index{
            String NAME_IDX = "productsNameIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

}
