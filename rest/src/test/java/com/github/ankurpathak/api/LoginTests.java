package com.github.ankurpathak.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.mongo.MongoDataRule;
import com.github.ankurpathak.api.security.DomainContextRule;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import com.github.ankurpathak.api.util.WebUtil;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {LoginTests.Initializer.class})
public class LoginTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private MongoTemplate mongoTemplate;


    //@RegisterExtension
    @ClassRule
    public static MongoDbContainer mongo = new MongoDbContainer();

    //@RegisterExtension
    @Rule
    public MongoDataRule mongoDataRule = new MongoDataRule(this);


    //@RegisterExtension
    @ClassRule
    public static RedisContainer redis = new RedisContainer();


    //@RegisterExtension
    @Rule
    public DomainContextRule domainContextRule = new DomainContextRule("103.51.209.45");;

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
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
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
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
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
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
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
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));


    }

    @Test
    public void anonymousUserCanNotLogin() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("1", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }

    @Test
    public void disabledUserCanNotLogin() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("amarmule@live.in", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", (String)null))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, ""))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    String.format("spring.data.mongodb.uri=mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort()),
                    String.format("spring.redis.url=redis://%s:%d", redis.getContainerIpAddress(), redis.getPort())
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}



