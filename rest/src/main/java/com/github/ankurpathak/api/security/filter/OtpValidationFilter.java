package com.github.ankurpathak.api.security.filter;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.security.authentication.token.PreOtpAuthenticationToken;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.service.IFilterService;
import com.github.ankurpathak.api.service.ITokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class OtpValidationFilter extends GenericFilterBean {

    public static final String DEFAULT_OTP_PARAMETER_NAME = "otptoken";
    public String otpParameterName = DEFAULT_OTP_PARAMETER_NAME;
    private String endpoint = "/otp";
    private final IFilterService filterService;
    private final ITokenService tokenService;


    public OtpValidationFilter(IFilterService filterService, ITokenService tokenService) {
        this.filterService = filterService;
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestHttp = (HttpServletRequest) request;
        HttpServletResponse responseHttp= (HttpServletResponse) response;

        // Make sure validation endpoint was requested before continuing
        String path = requestHttp.getRequestURI().substring(requestHttp.getContextPath().length());
        if (!path.equals(endpoint)) {
            chain.doFilter(request, response);
            return;
        }

        // Get token from request
        String token = request.getParameter(otpParameterName);
        if (StringUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Get username from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!(auth instanceof PreOtpAuthenticationToken)) {
            chain.doFilter(request, response);
            return;
        }
        PreOtpAuthenticationToken authToken = (PreOtpAuthenticationToken) auth;

        if(!(authToken.getPrincipal() instanceof CustomUserDetails)){
            chain.doFilter(request, response);
            return;
        }
        String phone = ((CustomUserDetails) authToken.getPrincipal()).getUser().getPhone().getValue();

        // Validate token
        Token.TokenStatus tokenStatus = tokenService.checkPhoneTokenStatus(phone, token);
        if (Objects.equals(tokenStatus, Token.TokenStatus.VALID)) {
            SecurityContextHolder.getContext().setAuthentication(authToken.getAuthentication());
            filterService.generateSuccess(responseHttp);
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            filterService.generateUnauthorized(responseHttp);
        }
    }
}