package com.ankurpathak.springsessiontest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {
    public static Optional<User> getMe() {
        return getMe(getAuthentication().orElse(null));
    }



    public static Optional<User> getMe(Authentication authentication){
        Optional<CustomUserDetails> userContext = getCustomUserDetails(authentication);
        if (userContext.isPresent() && userContext.get().getUser() != null) {
            return Optional.of(userContext.get().getUser());
        }
        return Optional.empty();
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
        return getDomainContext(getAuthentication().orElse(null));
    }

    public static Optional<DomainContext> getDomainContext(Authentication authentication) {
        if(authentication!= null){
            if(authentication.getDetails() != null && authentication.getDetails() instanceof DomainContext){
                DomainContext domainContext = (DomainContext) authentication.getDetails();
                return Optional.of(domainContext);
            }
        }
        return Optional.empty();
    }



    public static Optional<Authentication> getAuthentication() {
        return getAuthentication(SecurityContextHolder.getContext().getAuthentication());
    }

    public static Optional<Authentication> getAuthentication(Authentication authentication){
        if (authentication!= null) {
            return Optional.of(authentication);
        }
        return Optional.empty();
    }

    public static Optional<CustomUserDetails> getCustomUserDetails() {
        return getCustomUserDetails(getAuthentication().orElse(null));
    }



    public static Optional<CustomUserDetails> getCustomUserDetails(Authentication authentication) {
        if(authentication!= null){
            if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof CustomUserDetails){
                return Optional.of((CustomUserDetails)authentication.getPrincipal());
            }
        }
        return Optional.empty();
    }


    public static Optional<SecurityContextCompositeImpl> getSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context!= null && context instanceof SecurityContextCompositeImpl){
            return Optional.of((SecurityContextCompositeImpl) context);
        }
        return Optional.empty();
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
}
