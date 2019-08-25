package com.github.ankurpathak.api.constant;

public interface Model {

    interface Domain {
        interface Field {
            String ID = "_id";
            String CREATED = "created";
            String UPDATED = "updated";
            String CANDIDATE_ID = "candidateKey";
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }
    }

    interface ExtendedDomain {
        interface Field  extends Domain.Field {
            String CREATED_By = "createdBy";
            String UPDATED_By = "updatedBy";
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }
    }

    interface BusinessExtendedDomain {
        interface Field  extends ExtendedDomain.Field {
            String BUSINESS_ID = "businessId";
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }
    }

    interface User {

        String USER = "user";

        interface Field extends ExtendedDomain.Field {
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


            interface Definition  {
                String EMAIL_IDX_DEF = "{ 'email.value' : 1}";
                String PHONE_IDX_DEF = "{ 'phone.value' : 1}";
            }
        }

        interface Query { }
    }


    interface Role {

        String ROLE = "role";

        interface Field extends Domain.Field { }

        interface Index {
            String ROLE_NAME_IDX = "rolesNameIdx";
            interface Definition{ }
        }
        interface Query { }

    }


    interface Token {
        String TOKEN = "token";

        interface Field extends Domain.Field{
            String VALUE = "value";
        }

        interface Index extends Domain.Index{
            String EXPIRY_IDX = "tokenExpiryIdx";
            interface Definition { }
        }
        interface Query{ }
    }

    interface Sequence {
        String SEQUENCE = "sequence";

        interface Field extends Domain.Field{
            String CURRENT = "curr";
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }


    }

    interface Business {
        String BUSINESS = "business";

        interface Field extends ExtendedDomain.Field {
            String CURRENT = "curr";
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }

    }

    interface Mail {
        String MAIL = "mail";

        interface Field extends BusinessExtendedDomain.Field{
        }

        interface Index {
            interface Definition  { }
        }

        interface Query{ }

    }


    interface Product {
        String PRODUCT = "product";

        interface Field extends BusinessExtendedDomain.Field {
            String NAME  = "name";
        }

        interface Index {
            String NAME_IDX = "productNameIdx";

            interface Definition { }
        }

        interface Query{ }

    }

    interface Text {
        String TEXT = "text";

        interface Field extends Domain.Field{
        }

        interface Index {
            interface Definition { }
        }

        interface Query { }

    }

    interface Customer {
        String CUSTOMER = "customer";

        interface Field extends ExtendedDomain.Field{
            String NAME  = "name";
            String BUSINESS_ID  = "_id.businessId";
            String USER_ID  = "_id.userId";
        }

        interface Index{
            interface Definition { }
        }

        interface Query { }

    }

    interface VUserBusiness {
        String V_USER_BUSINESS = "userBusiness";

        interface Field extends User.Field { }

        interface Index {
            interface Definition { }
        }

        interface Query { }

    }


    interface VCustomerUser {
        String V_CUSTOMER_USER = "customerUser";

        interface Field extends Customer.Field { }

        interface Index{

            interface Definition  { }
        }
        interface Query{ }
    }

    interface Task {
        String TASK = "task";
        interface Field extends Domain.Field { }
        interface Index{
            interface Definition  { }
        }
        interface Query{ }
    }
}
