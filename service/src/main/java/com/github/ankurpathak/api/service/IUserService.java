package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.model.VUserBusiness;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IUserService  extends IDomainService<User, BigInteger> {
    Optional<VUserBusiness> byEmail(String email);
    Optional<VUserBusiness> byPhone(String contact);
    Map<String, Object> possibleCandidateKeys(String username);
    Set<String> possibleContacts(String username);
    User processUserForCustomer(Business business, CustomerDto customerDto);
    List<User> processUserForCustomers(Business business, Map<String, CustomerDto> customerDtosMap);
}
