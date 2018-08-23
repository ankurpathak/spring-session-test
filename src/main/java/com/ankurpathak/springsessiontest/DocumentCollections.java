package com.ankurpathak.springsessiontest;

public interface DocumentCollections {

    String USERS = "users";
    String ROLES = "roles";



    interface Index {
        String USERS_EMAIL_IDX = "usersEmailIdx";
        String ROLES_NAME_IDX = "rolesNameIdx";
        String USERS_USERNAME_IDX = "usersUsernameIdx";
        String USERS_EMAIL_IDX_DEF = "{\"email.value\" : 1}";
        String USERS_CONTACT_IDX_DEF = "{\"contact.value\" : 1}";
        String USERS_CONTACT_IDX = "usersContactIdx";

    }
}
