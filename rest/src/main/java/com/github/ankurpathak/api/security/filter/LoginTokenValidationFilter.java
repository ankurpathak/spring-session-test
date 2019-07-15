package com.github.ankurpathak.api.security.filter;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.security.authentication.token.PreLoginTokenAuthenticationToken;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.service.IFilterService;
import com.github.ankurpathak.api.service.ITokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class LoginTokenValidationFilter extends GenericFilterBean {

    private final IFilterService filterService;
    private final ITokenService tokenService;
    private final RequestMatcher requiresAuthenticationRequestMatcher;
    private final UriTemplate uriTemplate;

    public LoginTokenValidationFilter(String defaultFilterProcessesUrl, IFilterService filterService, ITokenService tokenService) {
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(defaultFilterProcessesUrl);
        this.filterService = filterService;
        this.tokenService = tokenService;
        this.uriTemplate = new UriTemplate(defaultFilterProcessesUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestHttp = (HttpServletRequest) request;
        HttpServletResponse responseHttp= (HttpServletResponse) response;

        // Make sure validation endpoint was requested before continuing
        if (!this.requiresAuthenticationRequestMatcher.matches(requestHttp)) {
            chain.doFilter(request, response);
            return;
        }

        // Get token from request
        String path = requestHttp.getRequestURI().substring(requestHttp.getContextPath().length());
        Map<String, String> pathParams = uriTemplate.match(path);
        String tokenValue = pathParams.get(Params.Path.TOKEN);

        if (StringUtils.isEmpty(tokenValue)) {
            chain.doFilter(request, response);
            return;
        }

        // Get username from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!(auth.getClass().isAssignableFrom(PreLoginTokenAuthenticationToken.class))) {
            chain.doFilter(request, response);
            return;
        }
        PreLoginTokenAuthenticationToken authToken = (PreLoginTokenAuthenticationToken) auth;

        if(authToken.getPrincipal() == null){
            chain.doFilter(request, response);
            return;
        }

        if(!(authToken.getPrincipal().getClass().isAssignableFrom(CustomUserDetails.class))){
            chain.doFilter(request, response);
            return;
        }
        String phone = ((CustomUserDetails) authToken.getPrincipal()).getUser().getPhone().getValue();

        // Validate token
        Token.TokenStatus tokenStatus = tokenService.checkPhoneTokenStatus(phone, tokenValue);
        if (Objects.equals(tokenStatus, Token.TokenStatus.VALID)) {
            SecurityContextHolder.getContext().setAuthentication(authToken.getAuthentication());
            filterService.generateSuccess(responseHttp);
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            if(Objects.equals(tokenStatus, Token.TokenStatus.EXPIRED))
                filterService.generateExpiredToken(tokenValue, responseHttp);
            else if  (Objects.equals(tokenStatus, Token.TokenStatus.INVALID))
                filterService.generateInvalid(Params.Path.TOKEN, tokenValue, ApiCode.INVALID_TOKEN, responseHttp);
            else
                filterService.generateUnauthorized(responseHttp);
        }
    }
}