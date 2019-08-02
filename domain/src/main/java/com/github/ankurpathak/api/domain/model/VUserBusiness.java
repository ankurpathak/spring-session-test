package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VUserBusiness extends User implements Serializable {

    public static final VUserBusiness V_ANONYMOUS_USER;

    static {
        V_ANONYMOUS_USER = getInstance()
                .id(ANONYMOUS_USER.getId())
                .username(ANONYMOUS_USER.getUsername())
                .roles(ANONYMOUS_USER.getRoles())
                .enabled(false);


    }

    private List<Business> businesses;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }



    public static VUserBusiness getInstance(){
        return new VUserBusiness();
    }

    @Override
    public VUserBusiness id(BigInteger id) {
        super.id(id);
        return this;
    }

    @Override
    public VUserBusiness roles(Set<String> roles) {
        super.roles(roles);
        return this;
    }

    @Override
    public VUserBusiness enabled(boolean enabled) {
        super.enabled(enabled);
        return this;
    }

    @Override
    public VUserBusiness username(String username) {
        super.username(username);
        return this;
    }

    public VUserBusiness(){

    }

    public VUserBusiness(User user){
        BeanUtils.copyProperties(user, this, "businesses");
    }

    public static VUserBusiness getInstance(User user){
       return new VUserBusiness(user);
    }

    public VUserBusiness businesses(List<Business> businesses) {
        this.businesses = businesses;
        return this;
    }
}
