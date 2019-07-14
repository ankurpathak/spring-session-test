package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.domain.repository.mongo.IUserRepository;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.service.IpService;
import com.github.ankurpathak.api.util.LogUtil;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import com.github.ankurpathak.primitive.string.StringValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;


@Service
public class UserService extends AbstractDomainService<User, BigInteger> implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    private final IUserRepository dao;
    private final IpService ipService;
    private final CountryCacheService countryService;

    public UserService(IUserRepository dao, IpService ipService, CountryCacheService countryService) {
        super(dao);
        this.dao = dao;
        this.ipService = ipService;
        this.countryService = countryService;
    }


    @Override
    public Optional<User> byEmail(String email) {
        require(email, not(emptyString()));
        return dao.findByCriteria(Criteria.where(Model.User.Field.EMAIL_VALUE).is(email), PageRequest.of(0, 1), User.class)
                .findFirst();
    }

    @Override
    public Optional<User> byPhone(String phone) {
        require(phone, not(emptyString()));
        return dao.findByCriteria(Criteria.where(Model.User.Field.PHONE_VALUE).is(phone), PageRequest.of(0, 1), User.class)
                .findFirst();
    }



    @Override
    public Map<String, Object> possibleCandidateKeys(String username) {
        if (StringUtils.isEmpty(username))
            return Collections.emptyMap();
        Map<String, Object> possibleKeys = new LinkedHashMap<>();

        if (StringValidator.email(username, false))
            possibleKeys.put(Params.EMAIL, username);
        else if (StringValidator.contact(username, false))
            possibleKeys.put(Params.PHONE, username);

        BigInteger possibleId = PrimitiveUtils.toBigInteger(username);
        if (possibleId.compareTo(BigInteger.ZERO) > 0) {
            possibleKeys.put(Params.MONGO_ID, possibleId);
        }

        possibleKeys.put(Params.USERNAME, username);
        return possibleKeys;
    }

    @Override
    public Set<String> possibleContacts(String username) {
        require(username, not(emptyString()));
        if (!StringUtils.isNumeric(username))
            return Collections.emptySet();
        Set<String> possibleContacts = new LinkedHashSet<>();
        SecurityUtil.getDomainContext()
                .map(DomainContext::getRemoteAddress)
                .flatMap(ipService::ipToCountryAlphaCode)
                .map(countryService::alphaCodeToCallingCodes)
                .ifPresent(callingCodes -> {
                    callingCodes.stream()
                            .map(callingCode -> String.format("+%s%s", callingCode, username))
                            .forEach(possibleContacts::add);
                });
        return possibleContacts;
    }


    @Override
    public User create(User entity) {
        require(entity, notNullValue());
        return dao.persist(entity);
    }
}
