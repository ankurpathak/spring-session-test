package com.ankurpathak.springsessiontest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    public static final String USERNAME_NOT_FOUND_MESSAGE = "Username %s not found.";

    private final IRoleService roleService;
    private final IUserService userService;

    public CustomUserDetailsService(IRoleService roleService, IUserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.byCandidateKey(username);
        if(user.isPresent()){
            return CustomUserDetails.getInstance(user.get(), getPrivileges(user.get().getRoles()));
        }else{
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username));
        }
    }


    private Set<String> getPrivileges(Set<String> roles){
        Set<String> privileges;
        if(!CollectionUtils.isEmpty(roles)){
            privileges = new HashSet<>();
            for(String roleName: roles){
                Optional<Role> role = roleService.findByName(roleName);
                role.ifPresent(x -> privileges.addAll(x.getPrivileges()));
            }
        }else {
            privileges = Collections.emptySet();
        }
        return privileges;
    }






}
