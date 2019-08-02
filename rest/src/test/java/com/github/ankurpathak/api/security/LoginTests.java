package com.github.ankurpathak.api.security;


import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.model.VUserBusiness;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {LoginTests.Initializer.class})
public class LoginTests extends AbstractRestIntegrationTest<LoginTests> {
    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    @Test
    public void loginWithEmailAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithEmailAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "incorrectpassword");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));


    }


    @Test
    public void loginWithNotExistingUser() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak.ap@gmail.com", "incorrectpassword");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.USER_NOT_FOUND.getCode())));


    }

    @Test
    public void loginWithFullContactAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("+917385500660", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithFullContactAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("+917385500660", "incorrectpassword");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));

    }

    @Test
    public void loginWithContactAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("7385500660", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithIdAndCorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("2", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }

    @Test
    public void loginWithContactAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("7385500660", "incorrectpassword");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));

    }

    @Test
    public void loginWithIdAndIncorrectPassword() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("2", "incorrectpassword");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.BAD_CREDENTIALS.getCode())));


    }

    @Test
    public void anonymousUserCanNotLogin() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("1", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }

    @Test
    public void disabledUserCanNotLogin() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("amarmule@live.in", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("SESSION"))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.emptyString()))
                .andExpect(header().doesNotExist(Params.Header.X_AUTH_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.ACCOUNT_DISABLED.getCode())));
    }


    @Test
    public void loginWithEmailAndCorrectPasswordRememberMe() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).header(Params.Header.X_REMEMBER_ME, true).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(cookie().exists("remember-me"))
                .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

    }



    @Test
    public void loginWithRememberMe() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .header(Params.Header.X_REMEMBER_ME, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION"))
        .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
        .andExpect(cookie().exists("remember-me"))
        .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
        .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
        .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
        .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
        .andDo(result -> {
            String remeberMeTokenValue = result.getResponse().getHeader(Params.Header.X_REMEMBER_ME_TOKEN);
            remeberMeTokenValue = remeberMeTokenValue != null ? remeberMeTokenValue : "";
            mockMvc.perform(get(apiPath(PATH_ME))
                    .header(Params.Header.X_REMEMBER_ME_TOKEN, remeberMeTokenValue )
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(cookie().exists("SESSION"))
            .andExpect(cookie().path("SESSION", Matchers.not(Matchers.emptyString())))
            .andExpect(cookie().exists("remember-me"))
            .andExpect(cookie().path("remember-me", Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
            .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
            .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())));
        });

    }


    @Test
    public void accessWithRememberMeSession() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .header(Params.Header.X_REMEMBER_ME, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION"))
        .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
        .andExpect(cookie().exists("remember-me"))
        .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
        .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
        .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
        .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
        .andDo(rememberMeLoginResult -> {
            String rememberMeTokenValue = rememberMeLoginResult.getResponse().getHeader(Params.Header.X_REMEMBER_ME_TOKEN);
            mockMvc.perform(get(apiPath(PATH_ME))
                    .header(Params.Header.X_REMEMBER_ME_TOKEN, StringUtils.defaultString(rememberMeTokenValue))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(cookie().exists("SESSION"))
            .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
            .andExpect(cookie().exists("remember-me"))
            .andExpect(cookie().value("remember-me", Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
            .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(header().exists(Params.Header.X_REMEMBER_ME_TOKEN))
            .andExpect(header().string(Params.Header.X_REMEMBER_ME_TOKEN, Matchers.not(Matchers.emptyString())))
            .andExpect(jsonPath("$.id", greaterThan(1)))
            .andDo(rememberMeSessionResult -> {
                String xAuthTokenValue = rememberMeSessionResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN);
                mockMvc.perform(get(apiPath(PATH_ME))
                        .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(xAuthTokenValue))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(1)));

            });
        });

    }

    @Test
    public void existingPhoneGetOtpForLogin() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("+917385500660", null);
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
                .andDo(otpResult -> {
                    mockMvc.perform(get(apiPath(PATH_ME))
                            .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(otpResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN)))

                    )
                            .andDo(print())
                            .andExpect(status().isForbidden());

                });

                outputCapture.expect(containsString("Phone: +917385500660"));
                outputCapture.expect(containsString("Text:"));
                outputCapture.expect(containsString("is your otp for login"));
    }

    @Test
    public void  existingPhoneAllSameOtpTillExpiry() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("+917385500660", null);
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        String output = outputCapture.toString();
        Pattern pattern = Pattern.compile(Pattern.quote("Text: ") + "(.*?)" + Pattern.quote(" is your"));
        Matcher matcher = pattern.matcher(output);
        List<String> otps = new ArrayList<>();
        while (matcher.find()) {
            otps.add(matcher.group(1));
        }
        assertThat(otps).isNotEmpty().hasSize(3);
        assertEquals("All otps are equal", 1, otps.stream().distinct().count());
    }



    @Test
    public void newPhoneGetOtpForLogin() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("+917588011779", null);
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
                .andExpect(newPhoneContactResult-> {
                    mockMvc.perform(get(apiPath(PATH_ME))
                            .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(newPhoneContactResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN)))
                    )
                            .andDo(print())
                            .andExpect(status().isForbidden());
                });

        Optional<VUserBusiness> user = userDetailsService.getUserService().byPhone("+917588011779");

        assertTrue("New User exists", user.isPresent());
        assertFalse("New User is disabled", user.get().isEnabled());

        outputCapture.expect(containsString("Phone: +917588011779"));
        outputCapture.expect(containsString("Text:"));
        outputCapture.expect(containsString("is your otp for completing your registration."));
    }


    @Test
    public void  newPhoneAllSameOtpTillExpiry() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("+917588011779", null);
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())));

        Optional<VUserBusiness> user = userDetailsService.getUserService().byPhone("+917588011779");
        assertTrue("New User exists", user.isPresent());
        assertFalse("New User is disabled", user.get().isEnabled());

        String output = outputCapture.toString();
        Pattern pattern = Pattern.compile(Pattern.quote("Text: ") + "(.*?)" + Pattern.quote(" is your"));
        Matcher matcher = pattern.matcher(output);
        List<String> otps = new ArrayList<>();
        while (matcher.find()) {
            otps.add(matcher.group(1));
        }
        assertThat(otps).isNotEmpty().hasSize(3);
        assertEquals("All otps are equal", 1, otps.stream().distinct().count());
    }



    @Test
    public void existingPhoneLoginWithOtp() throws Exception{
        LoginRequestDto dto = new LoginRequestDto("+917385500660", null);
        mockMvc.perform(post(apiPath(PATH_LOGIN))
                .param("async", String.valueOf(false))
                .header(Params.Header.X_OTP_FLOW, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().value("SESSION", Matchers.not(Matchers.emptyString())))
                .andExpect(header().exists(Params.Header.X_AUTH_TOKEN))
                .andExpect(header().string(Params.Header.X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andExpect(header().doesNotExist(Params.Header.X_REMEMBER_ME_TOKEN))
                .andExpect(jsonPath("$.code", is(ApiCode.SUCCESS.getCode())))
                .andDo(otpResult -> {
                    mockMvc.perform(get(apiPath(PATH_ME))
                            .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(otpResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN)))

                    )
                            .andDo(print())
                            .andExpect(status().isForbidden());


                    outputCapture.expect(containsString("Phone: +917385500660"));
                    outputCapture.expect(containsString("Text:"));
                    outputCapture.expect(containsString("is your otp for login"));

                    String output = outputCapture.toString();
                    String otpToken = StringUtils.substringBetween(output, "Text: ", " is your otp for login.");

                    mockMvc.perform(post(apiPath(PATH_LOGIN_OTP), otpToken)
                            .param("async", String.valueOf(false))
                            .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(otpResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN)))
                    )
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andDo(loginResult -> {
                                mockMvc.perform(get(apiPath(PATH_ME))
                                        .header(Params.Header.X_AUTH_TOKEN, StringUtils.defaultString(otpResult.getResponse().getHeader(Params.Header.X_AUTH_TOKEN)))

                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());
                            });


                });




    }

}






