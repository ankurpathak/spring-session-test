package com.github.ankurpathak.api.security.service;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.model.VUserBusiness;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IRoleService;
import com.github.ankurpathak.api.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    public static final String USERNAME_NOT_FOUND_MESSAGE = "Username %s not found.";

    private final IRoleService roleService;
    private final IUserService userService;
    private final IMessageService messageService;


    public CustomUserDetailsService(IRoleService roleService, IUserService userService, IMessageService messageService) {
        this.roleService = roleService;
        this.userService = userService;
        this.messageService = messageService;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<VUserBusiness> user = findByCandidateKey(username);
        if (user.isPresent()) {
            return CustomUserDetails.getInstance(user.get(), getPrivileges(user.get().getRoles()));
        } else {

            throw new UsernameNotFoundException(
                    messageService.getMessage(ApiMessages.NOT_FOUND, User.class.getSimpleName(), Model.User.Field.CANDIDATE_ID, username)
            );
        }
    }


    public Optional<VUserBusiness> findByCandidateKey(String username){
        List<Criteria> criteriaList = new ArrayList<>();
        userService.possibleCandidateKeys(username)
                .forEach((key, value) -> criteriaList.add(Criteria.where(KEY_MAPPINGS.get(key)).is(value)));

        userService.possibleContacts(username)
                .forEach(contact -> criteriaList.add(Criteria.where(KEY_PHONE).is(contact)));

        Criteria criteria = new Criteria();
        if(!CollectionUtils.isEmpty(criteriaList))
            criteria.orOperator(criteriaList.toArray(new Criteria[]{}));

        // if you want to filter anonymous user in query
        //criteria.andOperator(Criteria.where(Model.User.CustomField.ENABLED).is(true));
        return userService.findByCriteriaPaginated(criteria, PageRequest.of(0, 1), VUserBusiness.class, Model.VUserBusiness.V_USER_BUSINESS)
                .stream()
          // if you want to filter anonymous user
          //      .filter(user -> !Objects.equals(user.getId(), BigInteger.ONE))
                .findFirst();
    }


    public Set<String> getPrivileges(Set<String> roles) {
        Set<String> privileges;
        if (!CollectionUtils.isEmpty(roles)) {
            privileges = new HashSet<>();
            for (String roleName : roles) {
                Optional<Role> role = roleService.findByName(roleName);
                role.ifPresent(x -> privileges.addAll(x.getPrivileges()));
            }
        } else {
            privileges = Collections.emptySet();
        }
        return privileges;
    }


    public static final String KEY_EMAIL = "email.value";
    public static final String KEY_PHONE = "phone.value";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ID = "_id";


    public static final Map<String, String> KEY_MAPPINGS = new LinkedHashMap<>();
    static {
        KEY_MAPPINGS.put(Params.MONGO_ID, KEY_ID);
        KEY_MAPPINGS.put(Params.USERNAME, KEY_USERNAME);
        KEY_MAPPINGS.put(Params.EMAIL, KEY_EMAIL);
        KEY_MAPPINGS.put(Params.PHONE, KEY_PHONE);
    }


    public IRoleService getRoleService() {
        return roleService;
    }

    public IUserService getUserService() {
        return userService;
    }
}
