package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
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
@ContextConfiguration(initializers = {ProductControllerTests.Initializer.class})
public class ProductControllerTests extends AbstractRestIntegrationTest<ProductControllerTests> {


    @Test
    public void testAddService() throws Exception {
        ProductDto dto = ProductDto
                .getInstance()
                .name("My Product")
                .variable(true)
                .tax(BigDecimal.valueOf(5));

        mockMvc.perform(post(apiPath(PATH_SERVICE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));
    }


}
