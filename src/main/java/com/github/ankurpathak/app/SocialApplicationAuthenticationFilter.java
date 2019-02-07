package com.github.ankurpathak.app;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
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

public class SocialApplicationAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String defaultFilterProcessesUrl;
    private boolean postOnly = true;

    public SocialApplicationAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        this.defaultFilterProcessesUrl = defaultFilterProcessesUrl;
    }

    public SocialApplicationAuthenticationFilter() {
        this("/login/social/application/*");
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }else{
            String provider = obtainProvider(request);
            String token = obtainToken(request);

            if (provider == null) {
                provider = "";
            }

            if (token == null) {
                token = "";
            }


            provider = provider.trim();
            token = token.trim();

            SocialApplicationAuthenticationToken authRequest = new SocialApplicationAuthenticationToken(provider, token);

            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    private String obtainToken(HttpServletRequest request) {
        Configuration config = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS);

        try {
            var doc = JsonPath.using(config).parse(IOUtils.toString(request.getReader()));
            return doc.read("$.token");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  null;
    }


    protected void setDetails(HttpServletRequest request,
                              SocialApplicationAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainProvider(HttpServletRequest request) {
        UriTemplate template = new UriTemplate("/login/social/application/{provider}");
        Map<String, String> uriParams = template.match(request.getRequestURI());
        return uriParams.get("provider");
    }

}
