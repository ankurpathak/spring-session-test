package com.github.ankurpathak.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.controller.rest.dto.ApiCode;
import com.github.ankurpathak.app.service.ISequenceService;
import com.github.ankurpathak.app.util.WebUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;


    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @RegisterExtension
    public DomainContextBeforeEachExtension domainContextBeforeEachExtension = new DomainContextBeforeEachExtension("103.51.209.45");


    @RegisterExtension
    public MongoCleanUpExtension mongoCleanUpExtension = new MongoCleanUpExtension(this, Sequence.class,User.class, Role.class);

    @Test
    public void loginWithEmailAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithEmailAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "incorrectpassword");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));


    }

    @Test
    public void loginWithFullContactAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("+917385500660", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithFullContactAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("+917385500660", "incorrectpassword");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));

    }

    @Test
    public void loginWithContactAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("7385500660", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithIdAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("2", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithContactAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("7385500660", "incorrectpassword");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));

    }

    @Test
    public void loginWithIdAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("2", "incorrectpassword");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));


    }

    @Test
    public void anonymousUserCanNotLogin() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("1", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }

    @Test
    public void disabledUserCanNotLogin() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("amarmule@live.in", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }




   @TestConfiguration
    public static class TestConfig {

        @Bean
        public LoginContextRefreshedListener loginContextRefreshedListener(ISequenceService sequenceService, IUserService userService, IRoleService roleService, ITokenService tokenService, PasswordEncoder passwordEncoder){
            return new LoginContextRefreshedListener(sequenceService, userService, roleService, passwordEncoder);
        }
    }

}
