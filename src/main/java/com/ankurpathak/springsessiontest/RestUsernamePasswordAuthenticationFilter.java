package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private boolean postOnly = true;
    private final ObjectMapper objectMapper;

    public RestUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request)  {
        return obtainLoginRequest(request).getPassword();
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return obtainLoginRequest(request).getUsername();
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
