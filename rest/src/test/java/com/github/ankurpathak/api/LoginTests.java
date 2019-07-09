package com.github.ankurpathak.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.redis.RedisDataRule;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.mongo.MongoDataRule;
import com.github.ankurpathak.api.security.DomainContextRule;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import com.github.ankurpathak.api.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.ankurpathak.api.constant.RequestMappingPaths.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private RedisTemplate<?, ?> redisTemplate;


    //@RegisterExtension
    @ClassRule
    public static MongoDbContainer mongo = new MongoDbContainer();

    //@RegisterExtension
    @Rule
    public MongoDataRule<LoginTests> mongoDataRule = new MongoDataRule<>(this);

    //@RegisterExtension
    @Rule
    public RedisDataRule<LoginTests> redisDataRule = new RedisDataRule<>(this);


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
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
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
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
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
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithIdAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("2", "password");
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
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
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }


    @Test
    public void loginWithEmailAndCorrectPasswordRememberMe() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post("/login").header(WebUtil.HEADER_X_REMEMBER_ME, true).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }



    @Test
    public void loginWithRememberMe() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post("/login")
                .header(WebUtil.HEADER_X_REMEMBER_ME, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION"))
        .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
        .andExpect(cookie().exists("remember-me"))
        .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
        .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
        .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
        .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
        .andDo(result -> {
            String remeberMeTokenValue = result.getResponse().getHeader(WebUtil.HEADER_X_REMEMBER_ME_TOKEN);
            remeberMeTokenValue = remeberMeTokenValue != null ? remeberMeTokenValue : "";
            mockMvc.perform(get(apiPath(PATH_GET_ME))
                    .header(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, remeberMeTokenValue )
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(cookie().exists("SESSION"))
            .andExpect(cookie().path("SESSION", Matchers.not(Matchers.emptyString())))
            .andExpect(cookie().exists("remember-me"))
            .andExpect(cookie().path("remember-me", Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
            .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
            .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())));
        });

    }


    @Test
    public void accessWithRememberMeSession() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post("/login")
                .header(WebUtil.HEADER_X_REMEMBER_ME, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION"))
        .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
        .andExpect(cookie().exists("remember-me"))
        .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
        .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
        .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
        .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
        .andDo(rememberMeLoginResult -> {
            String rememberMeTokenValue = rememberMeLoginResult.getResponse().getHeader(WebUtil.HEADER_X_REMEMBER_ME_TOKEN);
            mockMvc.perform(get(apiPath(PATH_GET_ME))
                    .header(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, StringUtils.defaultString(rememberMeTokenValue))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(cookie().exists("SESSION"))
            .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
            .andExpect(cookie().exists("remember-me"))
            .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
            .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(WebUtil.HEADER_X_REMEMBER_ME_TOKEN))
            .andExpect(header().string(WebUtil.HEADER_X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(jsonPath("$.id", greaterThan(1)))
            .andDo(rememberMeSessionResult -> {
                String xAuthTokenValue = rememberMeSessionResult.getResponse().getHeader(WebUtil.HEADER_X_AUTH_TOKEN);
                mockMvc.perform(get(apiPath(PATH_GET_ME))
                        .header(WebUtil.HEADER_X_AUTH_TOKEN, StringUtils.defaultString(xAuthTokenValue))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(1)));

            });
        });

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



