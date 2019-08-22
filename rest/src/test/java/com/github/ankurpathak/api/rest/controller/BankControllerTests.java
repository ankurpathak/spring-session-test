package com.github.ankurpathak.api.rest.controller;


import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.service.IBankEtlService;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.ankurpathak.api.constant.ApiPaths.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("dev")
public class BankControllerTests   {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBankNames() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }


    @Test
    public void testBankStates() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_STATE))
                .param(Params.Query.CODE, "HDFC")


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

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }


    @Test
    public void testBankIfscFound() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_IFSC), "INDB0000001")

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }



    @Test
    public void testBankIfscNotFound() throws Exception {
        mockMvc.perform(get(apiPath(PATH_BANK_IFSC), "ANKU")

        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}

