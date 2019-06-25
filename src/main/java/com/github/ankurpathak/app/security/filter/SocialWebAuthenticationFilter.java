package com.github.ankurpathak.app.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriTemplate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SocialWebAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String defaultFilterProcessesUrl;

    public SocialWebAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        this.defaultFilterProcessesUrl = defaultFilterProcessesUrl;
    }

    public SocialWebAuthenticationFilter() {
        this("/login/social/*");
    }




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String provider = obtainProvider(request);
        String code = obtainCode(request);

        if (provider == null) {
            provider = "";
        }

        if (code == null) {
            code = "";
        }


        provider = provider.trim();
        code = code.trim();

        SocialWebAuthenticationToken authRequest = new SocialWebAuthenticationToken(
                provider,code);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    protected void setDetails(HttpServletRequest request,
                              SocialWebAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainProvider(HttpServletRequest request) {
        UriTemplate template = new UriTemplate("/login/social/{provider}");
        Map<String, String> uriParams = template.match(request.getRequestURI());
        return uriParams.get("provider");
    }


    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter("code");
    }


}
