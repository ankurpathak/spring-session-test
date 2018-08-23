package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Document(collection = DocumentCollections.ROLES)
public class Role extends Domain<String> implements Serializable {

    @Indexed(name = DocumentCollections.Index.ROLES_NAME_IDX, unique = true, sparse = true)
    private String name;
    private Set<String> privileges;
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";


    @Override
    public DomainDto<String> toDto() {
        return null;
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
    }
}
