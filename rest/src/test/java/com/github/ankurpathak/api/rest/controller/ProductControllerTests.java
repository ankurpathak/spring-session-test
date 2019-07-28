package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
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
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.xml.validation.Validator;
import java.math.BigDecimal;
import java.util.List;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .variable(true);

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

    @Test // Enable Name Index on product To Test
    @Ignore
    public void testTwoAddProductWithSameName() throws Exception {
        ProductDto dto = ProductDto
                .getInstance()
                .name("My Product")
                .variable(true);

        mockMvc.perform(post(apiPath(PATH_SERVICE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));

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



    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Test
    public void testServicesCsv() throws Exception {
        Resource csv = new ClassPathResource("services.csv", this.getClass());
        MockMultipartFile csvFile = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());

        mockMvc.perform(multipart(apiPath(PATH_SERVICE_UPLOAD)).file(csvFile)
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));
    }

    @Test // Enable Name Index on product To Test
    @Ignore
    public void testServicesCsvWithDuplicates() throws Exception {
        Resource csv = new ClassPathResource("services-duplicates.csv", this.getClass());
        MockMultipartFile csvFile = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());

        mockMvc.perform(multipart(apiPath(PATH_SERVICE_UPLOAD)).file(csvFile)
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)));
    }


    @Test
    public void testEmptyCsvFile() throws Exception{
        Resource csv = new ClassPathResource("services-empty.csv", this.getClass());
        MockMultipartFile csvFile = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());

        mockMvc.perform(multipart(apiPath(PATH_SERVICE_UPLOAD)).file(csvFile)
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(5)));
    }

    @Test
    public void testMissingHeaderCsvFile() throws Exception{
        Resource csv = new ClassPathResource("services-missing-header.csv", this.getClass());
        MockMultipartFile csvFile = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());

        mockMvc.perform(multipart(apiPath(PATH_SERVICE_UPLOAD)).file(csvFile)
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(5)));
    }
}
