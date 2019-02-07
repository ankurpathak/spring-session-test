package com.github.ankurpathak.app;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.ensure;


public class CustomUserDetails implements UserDetails, OAuth2User, OidcUser {

    private final User user;
    private final Set<String> privileges;
    private final Map<String, Object> attributes;
    private final String name;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;


    public static final CustomUserDetails ANNONYMOUS_CUSTOM_USER_DETAILS = getInstance(User.ANONYMOUS_USER, Role.ANONYMOUS_ROLE.getPrivileges());


    public User getUser() {
        return user;
    }

    private CustomUserDetails(User user, Set<String> privileges) {
        this(user, privileges, null, null, null, null);
    }


    protected CustomUserDetails(){
        this(null, null, null, null, null, null);
    }

    private CustomUserDetails(User user, Set<String> privileges, String name, Map<String, Object> attributes){
        this(user,privileges,name,attributes, null, null);
    }

    private CustomUserDetails(User user, Set<String> privileges, String name, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        this.user = user;
        this.privileges = privileges;
        this.name = name;
        this.attributes = attributes;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }



    public static CustomUserDetails getInstance(User user, Set<String> privileges){
        ensure(user, notNullValue());
        ensure(privileges, allOf(notNullValue(),not(is(empty()))));
        return new CustomUserDetails(user, privileges);
    }

    public static CustomUserDetails getInstance(User user, Set<String> privileges, OAuth2User oauth2User){
        ensure(user, notNullValue());
        ensure(privileges, allOf(notNullValue(),not(is(empty()))));
        ensure(oauth2User, notNullValue());
        return new CustomUserDetails(user, privileges, oauth2User.getName(), oauth2User.getAttributes());
    }

    public static CustomUserDetails getInstance(CustomUserDetails userDetails, OidcUser oidcUser){
        ensure(userDetails, notNullValue());
        ensure(oidcUser, notNullValue());
        return new CustomUserDetails(userDetails.user, userDetails.privileges, oidcUser.getName(), oidcUser.getAttributes(), oidcUser.getIdToken(), oidcUser.getUserInfo());
    }





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(!CollectionUtils.isEmpty(privileges))
            return AuthorityUtils.createAuthorityList(privileges.toArray(new String[]{}));
        else
            return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.getAttributes();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }
}
