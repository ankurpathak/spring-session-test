package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.dto.LoginRequestDto;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AccountControllerTests.Initializer.class})
@ActiveProfiles("test")
public class AccountControllerTests extends AbstractRestIntegrationTest<AccountControllerTests> {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP)
            .withConfiguration(
                    GreenMailConfiguration.aConfig()
                            .withUser("test", "secret")
            );


    @Test
    public void testDuplicateRegistration() throws Exception{
        UserDto userDto = UserDto.getInstance()
                .firstName("Ashwani")
                .lastName("Rathore")
                .email("rathore.ashwani@gmail.com")
                .password("password")
                .confirmPassword("password");
        mockMvc.perform(post(apiPath(PATH_ACCOUNT), "rathore.ashwani@gmail.com")
                .param("async", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post(apiPath(PATH_ACCOUNT), "rathore.ashwani@gmail.com")
                .param("async", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(ApiCode.EMAIL_FOUND.getCode())));

        System.out.println();
    }

    @Test
    public void testRegistration() throws Exception{
        UserDto userDto = UserDto.getInstance()
                .firstName("Ashwani")
                .lastName("Rathore")
                .email("rathore.ashwani@gmail.com")
                .password("password")
                .confirmPassword("password");
        mockMvc.perform(post(apiPath(PATH_ACCOUNT), "rathore.ashwani@gmail.com")
                .param("async", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
                .andDo(print())
                .andExpect(status().isCreated());

        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertThat(emails).isNotEmpty();
        String forgetPasswordHtml = new MimeMessageParser(emails[0]).parse().getHtmlContent();
        Document document = Jsoup.parse(forgetPasswordHtml);

        Element elementOtp = document.getElementById("otp");
        String otp = elementOtp.text();

        LoginRequestDto dto = new LoginRequestDto("rathore.ashwani@gmail.com", "password");
        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());


        mockMvc.perform(put(apiPath(PATH_ACCOUNT_ENABLE), otp)
        )
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post(apiPath(PATH_LOGIN)).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAccountEnableEmail() throws Exception {
        UserDto userDto = UserDto.getInstance()
                .firstName("Ashwani")
                .lastName("Rathore")
                .email("rathore.ashwani@gmail.com")
                .password("password")
                .confirmPassword("password");
        mockMvc.perform(post(apiPath(PATH_ACCOUNT), "rathore.ashwani@gmail.com")
                .param("async", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
                .andDo(print())
                .andExpect(status().isCreated());

        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertThat(emails).isNotEmpty();

        mockMvc.perform(put(apiPath(PATH_ACCOUNT_EMAIL), "rathore.ashwani@gmail.com")
                .param("async", String.valueOf(false))
        );
        emails = greenMail.getReceivedMessages();
        assertThat(emails).hasSize(2);

    }


}
