package com.github.ankurpathak.app;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SecurityUtil {
    public static Optional<User> getMe() {
        return getMe(getAuthentication().orElse(null));
    }



    public static Optional<User> getMe(Authentication authentication){
        return getCustomUserDetails(authentication)
                .map(CustomUserDetails::getUser);
    }









    /*


    public static String getRequestedOrganization(){
        if (getDomainContext().isPresent()) {
            return getDomainContext().get().getRequestedOrganization();
        }
        return null;
    }

    public static String getRequestedUser(){
        if (getDomainContext().isPresent()) {
            return getDomainContext().get().getRequestedUser();
        }
        return null;
    }

    */


    public static Optional<DomainContext> getDomainContext() {
        return DomainContextHolder.getContext();
    }




    public static Optional<Authentication> getAuthentication() {
        return getAuthentication(SecurityContextHolder.getContext().getAuthentication());
    }

    public static Optional<Authentication> getAuthentication(Authentication authentication){
        return Optional.ofNullable(authentication);
    }

    public static Optional<CustomUserDetails> getCustomUserDetails() {
        return getCustomUserDetails(getAuthentication().orElse(null));
    }



    public static Optional<CustomUserDetails> getCustomUserDetails(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter(CustomUserDetails.class::isInstance)
                .map(CustomUserDetails.class::cast);
    }


    /*


    public static Optional<SecurityContextCompositeImpl> getSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context instanceof SecurityContextCompositeImpl){
            return Optional.of((SecurityContextCompositeImpl) context);
        }
        return Optional.empty();
    }

    */


    public static Optional<ExtendedSecurityContextImpl> getSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .filter(ExtendedSecurityContextImpl.class::isInstance)
                .map(ExtendedSecurityContextImpl.class::cast);

    }




/*


    public static String getOrganization() {
        String organizationId = getRequestedOrganization();
        if(StringUtils.isBlank(organizationId))
            organizationId = getMeOrganization();
        if(StringUtils.isBlank(organizationId))
            organizationId = "0";
        return organizationId;
    }


    public static String getMeOrganization(){
        Optional<User> me = getMe();
        if(me.isPresent() && me.get().getOrganization() != null)
            return String.valueOf(me.get().getOrganization().getId());
        return null;
    }

    public static boolean isMyOrganization(String myOrganizationId){
        return getOrganization().equals(myOrganizationId);
    }

    public static String getUser(){
        String userId = getRequestedUser();
        if(StringUtils.isBlank(userId)){
            userId = getMeId();
        }
        if(StringUtils.isBlank(userId))
            userId = "0";
        return userId;
    }








    public static Boolean isMe(String meId){
        return getUser().equals(meId);
    }




    public static String getMeId(){
        Optional<User> me = getMe();
        if(me.isPresent()  && me.get().getId()!= null)
            return String.valueOf(me.get().getId());
        else return null;
    }

    */

    static Map<String, Object> collectClaims(OidcIdToken idToken, OidcUserInfo userInfo) {
        Assert.notNull(idToken, "idToken cannot be null");
        Map<String, Object> claims = new HashMap();
        if (userInfo != null) {
            claims.putAll(userInfo.getClaims());
        }

        claims.putAll(idToken.getClaims());
        return claims;
    }
}
