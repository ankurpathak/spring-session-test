package com.github.ankurpathak.app.security.service;

import com.github.ankurpathak.app.constant.Params;
import com.github.ankurpathak.app.domain.model.Role;
import com.github.ankurpathak.app.domain.model.User;
import com.github.ankurpathak.app.security.dto.CustomUserDetails;
import com.github.ankurpathak.app.service.IRoleService;
import com.github.ankurpathak.app.service.IUserService;
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
    //private final IpService ipService;
    //private final CountryCacheService countryService;

    public CustomUserDetailsService(IRoleService roleService,
                                    IUserService userService
            //, IpService ipService,
                                  //  CountryCacheService countryService
    ) {
        this.roleService = roleService;
        this.userService = userService;
        //this.ipService = ipService;
       // this.countryService = countryService;
    }

    /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Criteria> criteriaList = new ArrayList<>();
        if (!StringUtils.isNumeric(username)) {
            criteriaList.add(Criteria.where("email.value").is(username));
            criteriaList.add(Criteria.where("username").is(username));
        } else {
            criteriaList.add(Criteria.where("_id").is(PrimitiveUtils.toBigInteger(username)));
            criteriaList.add(Criteria.where("contact.value").is(username));
            SecurityUtil.getDomainContext()
                    .map(DomainContext::getRemoteAddress)
                    .flatMap(ipService::ipToCountryAlphaCode)
                    .map(countryService::alphaCodeToCallingCodes)
                    .ifPresent(callingCodes -> {
                        callingCodes.stream()
                                .map(callingCode->String.format("+%s%s",callingCode, username))
                                .forEach(contact -> criteriaList.add(Criteria.where("contact.value").is(contact)));
                    });
        }

        Criteria criteria = new Criteria().orOperator(criteriaList.toArray(new Criteria[]{}));
        Optional<User> user = userService.findByCriteriaPaginated(criteria, PageRequest.of(0, 1), User.class)
                .stream()
                .findFirst();
        if (user.isPresent()) {
            return CustomUserDetails.getInstance(user.get(), getPrivileges(user.get().getRoles()));
        } else {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username));
        }
    }

    */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByCandidateKey(username);
        if (user.isPresent()) {
            return CustomUserDetails.getInstance(user.get(), getPrivileges(user.get().getRoles()));
        } else {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username));
        }
    }


    public Optional<User> findByCandidateKey(String username){
        List<Criteria> criteriaList = new ArrayList<>();
        userService.possibleCandidateKeys(username)
                .forEach((key, value) -> criteriaList.add(Criteria.where(KEY_MAPPINGS.get(key)).is(value)));

        userService.possibleContacts(username)
                .forEach(contact -> criteriaList.add(Criteria.where(KEY_PHONE).is(contact)));

        Criteria criteria = new Criteria();
        if(!CollectionUtils.isEmpty(criteriaList))
            criteria.orOperator(criteriaList.toArray(new Criteria[]{}));

        // if you want to filter anonymous user in query
        //criteria.andOperator(Criteria.where(Model.User.Field.ENABLED).is(true));
        return userService.findByCriteriaPaginated(criteria, PageRequest.of(0, 1), User.class)
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
