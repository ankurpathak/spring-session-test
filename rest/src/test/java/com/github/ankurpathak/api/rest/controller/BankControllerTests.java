package com.github.ankurpathak.api.rest.controller;


import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.service.IBankIfscEtlService;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AccountControllerTests.Initializer.class})
public class BankControllerTests  extends AbstractRestIntegrationTest<BankControllerTests> {

    @Autowired
    private IBankIfscEtlService bankIfscEtlService;


    @Before
    public void processIfscData() throws IOException {
        bankIfscEtlService.process();
    }

    @Test
    public void testBankNames() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK))
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }


    @Test
    public void testBankStates() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_STATE))
                .param(Params.Query.CODE, "HDFC")
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }




    @Test
    public void testBankDistricts() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_DISTRICT))
                .param(Params.Query.CODE, "HDFC")
                .param(Params.Query.STATE, "MAHARASHTRA")
                .with(authentication(token("ankurpathak@live.in")))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }


    @Test
    public void testBankBranches() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_BRANCH))
                .param(Params.Query.CODE, "HDFC")
                .param(Params.Query.STATE, "MAHARASHTRA")
                .param(Params.Query.DISTRICT, "PUNE")
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }


    @Test
    public void testBankIfscFound() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_IFSC), "INDB0000001")
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }



    @Test
    public void testBankIfscNotFound() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_IFSC), "ANKU")
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }






}

