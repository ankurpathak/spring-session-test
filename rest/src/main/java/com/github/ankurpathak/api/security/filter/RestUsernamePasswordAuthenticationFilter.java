package com.github.ankurpathak.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private boolean postOnly = true;
    private final ObjectMapper objectMapper;

    public RestUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUr, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUr));
    }

    @Override
    protected String obtainPassword(HttpServletRequest request)  {
     //   if(WebUtil.isAjax(request)){
            return obtainLoginRequest(request).getPassword();
     //   }else {
       //     return super.obtainPassword(request);
     //   }
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
       // if(WebUtil.isAjax(request))
            return obtainLoginRequest(request).getUsername();
       // else
       //     return super.obtainUsername(request);
    }


    private LoginRequestDto obtainLoginRequest(HttpServletRequest request) {
        LoginRequestDto loginRequest = (LoginRequestDto) request.getAttribute(LoginRequestDto.class.getName());
        if(loginRequest == null){
            try{
                loginRequest = this.objectMapper.readValue(request.getReader(), LoginRequestDto.class);
                request.setAttribute(LoginRequestDto.class.getName(), loginRequest);
            }catch (IOException ex){
                loginRequest = new LoginRequestDto("", "");
            }
        }
        return loginRequest;
    }

}
