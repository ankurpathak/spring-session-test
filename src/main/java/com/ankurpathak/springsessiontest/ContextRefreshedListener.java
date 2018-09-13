package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IUserService userService;
    private final IRoleService roleService;
    private final MongoTemplate mongoTemplate;

    public ContextRefreshedListener(IUserService userService, IRoleService roleService, MongoTemplate mongoTemplate) {
        this.userService = userService;
        this.roleService = roleService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @SuppressWarnings("all")
    public void onApplicationEvent(ContextRefreshedEvent event) {
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Role.class);
        try{
            userService.createAll(users);
        }catch (DuplicateKeyException ex){


        }

        try{
            roleService.createAll(roles);
        }catch (DuplicateKeyException ex1){

        }
    }


    public static final Set<User> users = new HashSet<>();
    public static final Set<Role> roles = new HashSet<>();


    static {
        users.add(User.getInstance().id(BigInteger.TWO).firstName("Ankur").lastName("Pathak").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("ankurpathak@live.in")).password("password"));
        users.add(User.getInstance().id(BigInteger.TWO.add(BigInteger.ONE)).firstName("Amar").lastName("Mule").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("amarmule@live.in")).password("password"));


        roles.add(Role.getInstance().id("1").name(Role.ROLE_ADMIN).addPrivilege(Role.Privilege.PRIV_ADMIN));
        roles.add(Role.getInstance().id("2").name(Role.ROLE_ANONYMOUS).addPrivilege(Role.Privilege.PRIV_ANONYMOUS));
        roles.add(Role.getInstance().id("3").name(Role.ROLE_USER).addPrivilege(Role.Privilege.PRIV_USER));
    }
}
