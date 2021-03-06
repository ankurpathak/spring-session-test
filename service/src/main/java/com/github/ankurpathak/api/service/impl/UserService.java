package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.domain.repository.mongo.IUserRepository;
import com.github.ankurpathak.api.exception.TooManyException;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.service.IpService;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import com.github.ankurpathak.primitive.string.StringValidator;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;


@Service
@Transactional(readOnly = true)
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
    public Optional<VUserBusiness> byEmail(String email) {
        require(email, not(emptyString()));
        return dao.findByCriteria(Criteria.where(Model.User.Field.EMAIL_VALUE).is(email), PageRequest.of(0, 1), VUserBusiness.class, Model.VUserBusiness.V_USER_BUSINESS)
                .stream().findFirst();
    }

    @Override
    public Optional<VUserBusiness> byPhone(String phone) {
        require(phone, not(emptyString()));
        return dao.findByCriteria(Criteria.where(Model.User.Field.PHONE_VALUE).is(phone), PageRequest.of(0, 1), VUserBusiness.class, Model.VUserBusiness.V_USER_BUSINESS)
                .stream().findFirst();
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
    @Transactional
    public User processUserForCustomer(Business business, CustomerDto customerDto) {
        Query query =  new Query();
        Criteria criteria = Criteria.where(Model.User.Field.PHONE_VALUE).is(customerDto.getPhone());
        Page<User> page = findByCriteriaPaginated(criteria, PageRequest.of(0, 1), User.class);
        if(page.getTotalElements() > 1)
            throw new TooManyException(customerDto);
        else if(page.getTotalElements() <=0 ){
            User user = User.getInstance()
                    .phone(Contact.getInstance(customerDto.getPhone()))
                    .addRole(Role.ROLE_ADMIN)
                    .enabled(false)
                    .addAddress(Address.getInstance(customerDto).tag(String.format(Address.TAG_ADDED_BY_BUSINESS, business.getId())))
                    .addTag(String.format(User.TAG_INVITED_BY_BUSINESS, business.getId()));

           return create(user);
        } else {
           User user =  page.getContent().get(0);
            Address address = Address.getInstance(customerDto)
                    .tag(String.format(Address.TAG_ADDED_BY_BUSINESS, business.getId()));
            user.addAddress(address);
           return update(user);
        }
    }


    @Override
    @Transactional
    public List<User> processUserForCustomers(Business business, Map<String, CustomerDto> customerDtosMap) {
        require(business, notNullValue());
        require(business, notNullValue());
        Query query =  new Query();
        Criteria criteria = Criteria.where(Model.User.Field.PHONE_VALUE).in(customerDtosMap.keySet());
        List<User> matchedUsers = findByCriteria(criteria, PageRequest.of(0, customerDtosMap.size()), User.class);
        Map<String, User> machedUserMap = matchedUsers.stream().collect(toMap(x -> x.getPhone().getValue(), Function.identity()));
        Set<String> allPhones = customerDtosMap.keySet();
        Set<String> matchedPhones = machedUserMap.keySet();
        Set<String> notMatchedPhones = SetUtils.difference(allPhones, matchedPhones);
        //process new phones
        List<User> nonMatchedUsers = new ArrayList<>();
        {
            for(String notMatchedPhone: notMatchedPhones){
                nonMatchedUsers.add(
                        User.getInstance()
                        .phone(Contact.getInstance(notMatchedPhone))
                        .addRole(Role.ROLE_ADMIN)
                        .enabled(false)
                        .addAddress(Address.getInstance(customerDtosMap.get(notMatchedPhone)).tag(String.format(Address.TAG_ADDED_BY_BUSINESS, business.getId())))
                        .addTag(String.format(User.TAG_INVITED_BY_BUSINESS, business.getId()))
                );
            }

            nonMatchedUsers = IteratorUtils.toList(createAll(nonMatchedUsers).iterator(), notMatchedPhones.size());
        }
        {
           for(String matchedPhone: matchedPhones){
               User matchedUser = machedUserMap.get(matchedPhone);
               matchedUser.addAddress(Address.getInstance(customerDtosMap.get(matchedPhone))
                       .tag(String.format(Address.TAG_ADDED_BY_BUSINESS, business.getId())));
               matchedUsers = dao.saveAll(matchedUsers);
           }
        }
        return ListUtils.union(matchedUsers, nonMatchedUsers);
    }


    @Override
    @Transactional
    public User create(User entity) {
        require(entity, notNullValue());
        return dao.persist(entity);
    }

    @Override
    @Transactional
    public Iterable<User> createAll(Iterable<User> entities) {
        return dao.persistAll(IterableUtils.toList(entities));
    }
}
