package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.util.WebUtil;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static com.github.ankurpathak.api.constant.RequestMappingPaths.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {PasswordControllerTests.Initializer.class})
@ActiveProfiles("test")
public class PasswordControllerTests extends AbstractRestIntegrationTest<PasswordControllerTests> {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP)
                                            .withConfiguration(
                                                    GreenMailConfiguration.aConfig()
                                                    .withUser("test", "secret")
                                            );
    @Autowired
    private IEmailService emailService;



    @Test
    public void testSmtp() throws Exception {
        emailService.sendTextEmail("ankurpathak@live.in", "Test", "Test");
        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertThat(emails).isNotNull().isNotEmpty();
        assertThat(emails[0].getSubject()).isEqualTo("Test");
        assertThat(GreenMailUtil.getBody(emails[0])).isEqualTo("Test");
    }


    @Test
    public void testForgetPassword() throws Exception {
        mockMvc.perform(put(apiPath(PATH_FORGET_PASSWORD_EMAIL), "ankurpathak@live.in")
                .param("async", String.valueOf(false))
        )
                .andDo(print())
                .andExpect(status().isOk());

        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertThat(emails).isNotEmpty();
        String forgetPasswordHtml = new MimeMessageParser(emails[0]).parse().getHtmlContent();
        Document document = Jsoup.parse(forgetPasswordHtml);
        Element elementOtp = document.getElementById("otp");
        String otp = elementOtp.text();

        mockMvc.perform(put(apiPath(PATH_FORGET_PASSWORD_ENABLE), otp)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(WebUtil.HEADER_X_AUTH_TOKEN))
                .andExpect(header().string(WebUtil.HEADER_X_AUTH_TOKEN, Matchers.not(Matchers.emptyString())))
                .andDo(forgetPasswordEnableResult -> {
                    UserDto userDto = UserDto.getInstance()
                            .password("password1")
                            .confirmPassword("password1");

                    mockMvc.perform(put(apiPath(PATH_FORGET_PASSWORD))
                            .header(WebUtil.HEADER_X_AUTH_TOKEN, StringUtils.defaultString(forgetPasswordEnableResult.getResponse().getHeader(WebUtil.HEADER_X_AUTH_TOKEN)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDto))
                    )
                            .andDo(print())
                            .andExpect(status().isOk());
                });
        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password1");
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"));
    }


    @Test
    public void testChangePassword() throws Exception{
        UserDto userDto = UserDto.getInstance()
                .password("password2")
                .confirmPassword("password2")
                .currentPassword("password");
        mockMvc.perform(patch(apiPath(PATH_CHANGE_PASSWORD))
                .with(authentication(token("ankurpathak@live.in")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
                .andDo(print())
                .andExpect(status().isOk());

        LoginRequestDto dto = new LoginRequestDto("ankurpathak@live.in", "password2");
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"));

    }



}