package com.github.ankurpathak.api.constant;

public interface Model {




    interface Domain {
        interface Field {
            String ID = "_id";
            String CREATED = "created";
            String UPDATED = "updated";
            String CANDIDATE_ID = "candidateKey";
            String BUSINESS_ID = "businessId";
        }

        interface Index {

            interface Definition {

            }
        }

        interface Query { }
    }

    interface User {

        String USER = "user";

        interface Field extends Domain.Field {
            String ENABLED = "enabled";
            String EMAIL = "email";
            String EMAIL_VALUE = "email.value";
            String PHONE = "phone";
            String PHONE_VALUE = "phone.value";
            String USERNAME = "username";

        }

        interface Index {
            String EMAIL_IDX = "userEmailIdx";
            String USERNAME_IDX = "userUsernameIdx";
            String PHONE_IDX = "userPhoneIdx";


            interface Definition extends Domain.Index.Definition {
                String EMAIL_IDX_DEF = "{ 'email.value' : 1}";
                String PHONE_IDX_DEF = "{ 'phone.value' : 1}";
            }
        }

        interface Query { }
    }


    interface Role {

        String ROLE = "role";

        interface Field extends Domain.Field { }

        interface Index extends Domain.Index{
            String ROLE_NAME_IDX = "rolesNameIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }


    interface Token {
        String TOKEN = "token";

        interface Field extends Domain.Field{
            String VALUE = "value";
        }

        interface Index extends Domain.Index{
            String EXPIRY_IDX = "tokenExpiryIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Sequence {
        String SEQUENCE = "sequence";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }


    }

    interface Business {
        String BUSINESS = "business";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Mail {
        String MAIL = "mail";

        interface Field extends Domain.Field{
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }


    interface Product {
        String PRODUCT = "product";

        interface Field extends Domain.Field{
            String NAME  = "name";
        }

        interface Index extends Domain.Index{
            String NAME_IDX = "productNameIdx";

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Text {
        String TEXT = "text";

        interface Field extends Domain.Field{
        }

        interface Index extends Domain.Index{
            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface Customer {
        String CUSTOMER = "customer";

        interface Field extends Domain.Field{
            String NAME  = "name";
            String BUSINESS_ID  = "_id.businessId";
            String USER_ID  = "_id.userId";
        }

        interface Index extends Domain.Index{

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

    interface VUserBusiness extends User {
        String V_USER_BUSINESS = "userBusiness";

        interface Field extends Domain.Field{

        }

        interface Index extends Domain.Index{

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }


    interface VCustomerUser extends Customer {
        String V_CUSTOMER_USER = "customerUser";

        interface Field extends Domain.Field{

        }

        interface Index extends Domain.Index{

            interface Definition extends Domain.Index.Definition { }
        }

        interface Query extends Domain.Query{ }

    }

}
