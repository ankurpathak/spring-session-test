package com.ankurpathak.springsessiontest;

public interface DocumentCollections {

    String USERS = "users";
    String ROLES = "roles";



    interface Index {
        String USERS_EMAIL_IDX = "usersEmailIdx";
        String ROLES_NAME_IDX = "rolesNamesIdx";
    }
}
