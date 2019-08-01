package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.BusinessDto;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_BUSINESS;
import static com.github.ankurpathak.api.constant.ApiPaths.apiPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {BusinessControllerTests.Initializer.class})
@ActiveProfiles("test")
public class BusinessControllerTests extends AbstractRestIntegrationTest<BusinessControllerTests> {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP)
            .withConfiguration(
                    GreenMailConfiguration.aConfig()
                            .withUser("test", "secret")
            );


    @Test
    public void testAddFirstBusiness() throws Exception {
        BusinessDto businessDto = BusinessDto.getInstance()
                .name("Gladiris Technologies Pvt Ltd")
                .type("RENTAL")
                .email("ankurpathak@outlook.com");

        mockMvc.perform(post(apiPath(PATH_BUSINESS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessDto))
                .with(authentication(token("+918000000005")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }


    @Test
    public void testAddTwoBusiness() throws Exception {
        BusinessDto businessDto = BusinessDto.getInstance()
                .name("Gladiris Technologies Pvt Ltd")
                .type("RENTAL")
                .email("ankurpathak@outlook.com");

        mockMvc.perform(post(apiPath(PATH_BUSINESS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessDto))
                .with(authentication(token("+918000000005")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.id", greaterThan(0)));

        businessDto = BusinessDto.getInstance()
                .name("Gladiris Technologies Pvt Ltd")
                .type("ED")
                .email("ankurpathak@outlook.com");

        mockMvc.perform(post(apiPath(PATH_BUSINESS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessDto))
                .with(authentication(token("+918000000005")))
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo(25)));
    }


    @Test
    public void testBusinessNotFound() throws Exception {
        BusinessDto businessDto = BusinessDto.getInstance()
                .name("Gladiris Technologies Pvt Ltd 1");

        mockMvc.perform(put(apiPath(PATH_BUSINESS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessDto))
                .with(authentication(token("+918000000005")))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(ApiCode.BUSINESS_NOT_FOUND.getCode())));
    }

    @Test
    public void testUpdateBusiness() throws Exception {
        BusinessDto businessDto = BusinessDto.getInstance()
                .name("Ankur Pvt Ltd");

        mockMvc.perform(put(apiPath(PATH_BUSINESS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessDto))
                .with(authentication(token("+919000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(ApiCode.SUCCESS.getCode())));

        System.out.println();
    }

}
