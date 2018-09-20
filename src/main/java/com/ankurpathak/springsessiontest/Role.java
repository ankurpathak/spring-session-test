package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Document(collection = DocumentCollections.ROLE)
public class Role extends Domain<String> implements Serializable {

    @Indexed(name = DocumentCollections.Index.ROLE_NAME_IDX, unique = true, sparse = true)
    private String name;
    private Set<String> privileges;
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";


    public static final Role ANONYMOUS_ROLE = Role.getInstance().name(ROLE_ANONYMOUS);


    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    @Override
    public Role id(String id) {
        super.id(id);
        return this;
    }


    public static Role getInstance(){
        return new Role();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }


    public Role name(String name) {
        this.name = name;
        return this;
    }

    public Role privileges(Set<String> privileges) {
        this.privileges = privileges;
        return this;
    }

    public Role addPrivilege(String privilege){
        if(privileges == null)
            privileges = new HashSet<>();
        if(!StringUtils.isEmpty(privilege))
            privileges.add(privilege);
        return this;
    }


    public Role removePrivilege(String privilege){
        if(!CollectionUtils.isEmpty(privileges))
            privileges.remove(privilege);
        return this;
    }



    public interface Privilege {
        String PRIV_ANONYMOUS = "PRIV_ANONYMOUS";
        String PRIV_USER = "PRIV_USER";
        String PRIV_ADMIN = "PRIV_ADMIN";
        String PRIV_ACCOUNT = "PRIV_ACCOUNT";
        String PRIV_ACCOUNT_EMAIL = "PRIV_ACCOUNT_EMAIL";
        String PRIV_ACCOUNT_ENABLE = "PRIV_ACCOUNT_ENABLE";
        String PRIV_FORGET_PASSWORD_EMAIL = "PRIV_FORGET_PASSWORD_EMAIL";
        String PRIV_FORGET_PASSWORD_ENABLE = "PRIV_FORGET_PASSWORD_ENABLE";
        String PRIV_FORGET_PASSWORD = "PRIV_FORGET_PASSWORD";

    }


    public String[] privilegesAsArray(){
        if(CollectionUtils.isEmpty(privileges)){
            return new String[0];
        }else {
           return privileges.toArray(new String[0]);
        }
    }


    static {
        ANONYMOUS_ROLE.privileges = Set.of(
                Privilege.PRIV_ACCOUNT,
                Privilege.PRIV_ACCOUNT_EMAIL,
                Privilege.PRIV_ACCOUNT_ENABLE,
                Privilege.PRIV_FORGET_PASSWORD_EMAIL,
                Privilege.PRIV_FORGET_PASSWORD_ENABLE
        );
    }
}
