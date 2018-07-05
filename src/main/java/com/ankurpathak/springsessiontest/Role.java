package com.ankurpathak.springsessiontest;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role extends Domain<String> implements Serializable {
    private String name;
    private Set<String> privileges = new HashSet<>();
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";


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
        if(privileges != null)
            privileges.add(privilege);
        return this;
    }


    public Role removePrivilege(String privilege){
        if(privileges != null)
            privileges.remove(privilege);
        return this;
    }



    public interface Privilege {

    }
}
