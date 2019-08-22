package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.SpringSessionTestApplication;
import com.github.ankurpathak.api.config.MongoConfig;
import com.github.ankurpathak.api.config.RedisConfig;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import com.github.ankurpathak.api.util.MatcherUtil;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.xml.validation.Validator;
import java.math.BigDecimal;
import java.util.List;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MongoConfig.class, RedisConfig.class, SpringSessionTestApplication.class})
@ActiveProfiles("test")
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



    @Test
    public void testServicesCsv() throws Exception {
        Resource csv = new ClassPathResource("service.csv", this.getClass());
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
        Resource csv = new ClassPathResource("service-with-duplicate.csv", this.getClass());
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
        Resource csv = new ClassPathResource("service-empty.csv", this.getClass());
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
        Resource csv = new ClassPathResource("service-missing-header.csv", this.getClass());
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
    public void testGetPaginated() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE))
                .param(Params.Query.SORT,"name,asc")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list", hasSize(lessThanOrEqualTo(20))))
                .andExpect(jsonPath("$.data.list", hasSize(greaterThan(0))));
    }


    @Test
    public void testGetSearchMissingRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(5)))
                .andExpect(jsonPath("$.message", containsString("rsql")));
    }

    @Test
    public void testGetSearchInvalidRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.SORT,"name,asc")
                .param(Params.Query.RSQL,"hello")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(14)))
                .andExpect(jsonPath("$.message", containsString("RSQL")));
    }


    @Test
    public void testGetSearchRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.SORT,"name,asc")
                .param(Params.Query.RSQL,"name==E")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.list[0].name", equalToIgnoringCase("E")));
    }


    @Test
    public void testGetSearchStartingWithRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.SORT,"name,asc")
                .param(Params.Query.RSQL,"name=s=E")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.list[0].name", startsWithIgnoringCase("E")));
    }


    @Test
    public void testGetSearchEndingWithRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.SORT,"name,asc")
                .param(Params.Query.RSQL,"name=e=E")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.list[0].name", endsWithIgnoringCase("E")));
    }



    @Test
    public void testGetSearchContaininghRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.SORT,"name,asc")
                .param(Params.Query.RSQL,"name=c=E")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.list[0].name", containsStringIgnoringCase("E")));
    }


    @Test
    public void testGetSearchBetweenTwoTimestampRsql() throws Exception{

        mockMvc.perform(get(apiPath(PATH_SERVICE_SEARCH))
                .param(Params.Query.RSQL,"created=between=(2019-08-02T08:00:22.084615Z,2019-08-02T11:00:22.084615Z)")
                .with(authentication(token("+918000000000")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.code", equalTo(0)))
                .andExpect(jsonPath("$.data.list", MatcherUtil.notCollectionEmpty()))
                .andExpect(jsonPath("$.data.list[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.list[0].name", containsStringIgnoringCase("E")));
    }









}
