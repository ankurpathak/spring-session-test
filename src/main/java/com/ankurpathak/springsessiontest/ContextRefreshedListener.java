package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IUserService userService;
    private final IRoleService roleService;
    private final MongoTemplate mongoTemplate;
    private final ITokenService tokenService;
    private final ISequenceRepository sequenceRepository;

    public ContextRefreshedListener(IUserService userService, IRoleService roleService, MongoTemplate mongoTemplate, ITokenService tokenService, ISequenceRepository sequenceRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.mongoTemplate = mongoTemplate;
        this.tokenService = tokenService;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    @SuppressWarnings("all")
    public void onApplicationEvent(ContextRefreshedEvent event) {

        userService.deleteAll();
        roleService.deleteAll();
        tokenService.deleteAll();
        sequenceRepository.deleteAll();
        sequenceRepository.insert(Sequence.getUserSequenceInitialValue());


        try{
            for (User user:
                users ) {
                userService.create(user);
            }
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
        users.add(User.getInstance().firstName("Ankur").lastName("Pathak").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("ankurpathak@live.in")).password(Password.getInstance().value("password")));
        users.add(User.getInstance().firstName("Amar").lastName("Mule").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("amarmule@live.in")).password(Password.getInstance().value("password")));
        users.add(User.getInstance().firstName("Pradeep").lastName("Negi").addRole(Role.ROLE_ADMIN).email(Contact.getInstance("ankurpathak.ap@gmail.com")).password(Password.getInstance().value("password")));


        roles.add(Role.getInstance().id("1").name(Role.ROLE_ADMIN).addPrivilege(Role.Privilege.PRIV_ADMIN));
        roles.add(Role.getInstance().id("2").name(Role.ROLE_ANONYMOUS).addPrivilege(Role.Privilege.PRIV_ANONYMOUS));
        roles.add(Role.getInstance().id("3").name(Role.ROLE_USER).addPrivilege(Role.Privilege.PRIV_USER));
    }
}
