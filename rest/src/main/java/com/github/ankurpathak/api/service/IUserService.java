package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.PhoneEmailPairDto;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IUserService  extends IDomainService<User, BigInteger> {
    Optional<User> byEmail(String email);
    Optional<User> byPhone(String contact);
    Map<String, Object> possibleCandidateKeys(String username);
    Set<String> possibleContacts(String username);

    User processUserForCustomer(Business business, CustomerDto customerDto);
}
