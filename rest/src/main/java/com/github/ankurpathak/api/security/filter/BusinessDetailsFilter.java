package com.github.ankurpathak.api.security.filter;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import com.github.ankurpathak.api.security.exception.BusinessDetailsException;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.util.WebUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class BusinessDetailsFilter extends OncePerRequestFilter {
    private final IBusinessService businessService;

    public BusinessDetailsFilter(IBusinessService businessService) {
        this.businessService = businessService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Optional<User> user = SecurityUtil.getMe();
        if (user.isPresent()) {
            if (!Objects.equals(user.get().getId(), BigInteger.ONE)) { //Not Anonymous User
                if (CollectionUtils.isNotEmpty(user.get().getBusinessIds())) {  // User has a business
                    Optional<DomainContext> context = DomainContextHolder.getContext();
                    if (context.isPresent()) { // Domain Context Found
                        BigInteger requestedBusinessId = context.get().getRequestedBusinessId();

                        if (user.get().getBusinessIds().size() == 1 && Objects.equals(requestedBusinessId, BigInteger.ZERO)) { // No Business Send And User has one Business Consider It
                            Optional<BigInteger> userOnlyBusinessId = user.get().getBusinessIds().stream().findFirst();
                            if (userOnlyBusinessId.isPresent()) {
                                requestedBusinessId = userOnlyBusinessId.get();
                            }
                        }
                        Optional<Business> business = businessService.findById(requestedBusinessId);
                        if (business.isPresent() && CollectionUtils.containsAny(user.get().getBusinessIds(), business.get().getId())) { // Requested Business is Mine

                            try {
                                context.get().setBusiness(business.get());
                                chain.doFilter(request, response);
                            } finally {
                                context.get().setBusiness(null);
                            }
                        } else { // Requested Business is not mine
                            throw new BusinessDetailsException("Requested Business is Not Mine");
                        }
                    }else{ // No DomainContext Found
                        throw new BusinessDetailsException("DomainContext Not Found to hold Business");
                    }
                }else {// User don't have any business
                    chain.doFilter(request, response);
                    return;
                }
            } else { //Anonymous User
                chain.doFilter(request, response);
                return;
            }
        } else {
            throw new BusinessDetailsException("No User Found to Match Business");
        }
    }
}




