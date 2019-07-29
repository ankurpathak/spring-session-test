package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {CustomerControllerTests.Initializer.class})
public class CustomerControllerTests extends AbstractRestIntegrationTest<CustomerControllerTests> {


    @Test
    public void testAddCustomerExistingUser() throws Exception {
        CustomerDto dto = CustomerDto
                .getInstance()
                .phone("+917385500660")
                .name("Test Name")
                .address("TestAddress")
                .state("TestState")
                .city("TestCity")
                .pinCode("TestPinCode");

        mockMvc.perform(post(apiPath(PATH_CUSTOMER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));
    }

    @Test
    public void testAddCustomerNewUser() throws Exception {
        CustomerDto dto = CustomerDto
                .getInstance()
                .phone("+918087715135")
                .name("Test Name")
                .address("TestAddress")
                .state("TestState")
                .city("TestCity")
                .pinCode("TestPinCode");

        mockMvc.perform(post(apiPath(PATH_CUSTOMER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));
    }


    @Test
    public void testCustomersCsv() throws Exception{

    }


}
