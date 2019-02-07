package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.model.Contact;
import com.github.ankurpathak.app.service.ISequenceService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashSet;
import java.util.Set;


public class LoginContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ISequenceService sequenceService;
    private final IUserService userService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public LoginContextRefreshedListener(ISequenceService sequenceService, IUserService userService, IRoleService roleService, PasswordEncoder passwordEncoder) {
        this.sequenceService = sequenceService;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        addUsers();
    }

    @Override
    @SuppressWarnings("all")
    public void onApplicationEvent(ContextRefreshedEvent event) {
        sequenceService.deleteAll();
        sequenceService.init();
        users.forEach(userService::create);
        roleService.createAll(roles);
    }


    private final Set<User> users = new LinkedHashSet<>();
    private final Set<Role> roles = new LinkedHashSet<>();


    private void addUsers(){
        users.add(User.ANONYMOUS_USER);
        users.add(User.getInstance().firstName("Ankur").lastName("Pathak").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("ankurpathak@live.in")).contact(Contact.getInstance("+917385500660")).password(Password.getInstance().value(passwordEncoder.encode("password"))).enabled(true));
        users.add(User.getInstance().firstName("Amar").lastName("Mule").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("amarmule@live.in")).password(Password.getInstance().value(passwordEncoder.encode("password"))));
        roles.add(Role.getInstance().id("1").name(Role.ROLE_ANONYMOUS).addPrivilege(Role.Privilege.PRIV_ANONYMOUS));
        roles.add(Role.getInstance().id("2").name(Role.ROLE_ADMIN).addPrivilege(Role.Privilege.PRIV_ADMIN).addPrivilege(Role.Privilege.PRIV_CHANGE_PASSWORD).addPrivilege(Role.Privilege.PRIV_CHANGE_PROFILE));
        roles.add(Role.getInstance().id("3").name(Role.ROLE_USER).addPrivilege(Role.Privilege.PRIV_USER));
    }
}
