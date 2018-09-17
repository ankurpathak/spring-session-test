package com.ankurpathak.springsessiontest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.ensure;


public class CustomUserDetails implements UserDetails {

    private User user;
    private Set<String> privileges;


    public static final CustomUserDetails ANNONYMOUS_CUSTOM_USER_DETAILS = getInstance(User.ANONYMOUS_USER, Role.ANONYMOUS_ROLE.getPrivileges());


    public User getUser() {
        return user;
    }

    public CustomUserDetails(User user, Set<String> privileges) {
        ensure(user, notNullValue());
        ensure(privileges, not(is(empty())));
        this.user = user;
        this.privileges = privileges;
    }


    public static CustomUserDetails getInstance(User user, Set<String> privileges){
        return new CustomUserDetails(user, privileges);
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(!CollectionUtils.isEmpty(privileges))
            return AuthorityUtils.createAuthorityList(privileges.toArray(new String[user.getRoles().size()]));
        else
            return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        ensure(user.getPassword(), notNullValue());
        return user.getPassword().getValue();
    }

    @Override
    public String getUsername() {

        return String.valueOf(user.getId());
       // return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}
