package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.constant.Params;
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
public class CityControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testStates() throws Exception {
        mockMvc.perform(get(apiPath(PATH_STATE))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }





    @Test
    public void testStateDistricts() throws Exception {
        mockMvc.perform(get(apiPath(PATH_STATE_DISTRICT))
                .param(Params.Query.STATE, "MAHARASHTRA")


        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }




    @Test
    public void testPinCodes() throws Exception {
        mockMvc.perform(get(apiPath(PATH_STATE_PIN_CODE))
                .param(Params.Query.STATE, "MAHARASHTRA")
                .param(Params.Query.DISTRICT, "Pune")
                .param(Params.Query.PAGE, String.valueOf(0))
                .param(Params.Query.SIZE, String.valueOf(25))

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }




    @Test
    public void testDistricts() throws Exception {
        mockMvc.perform(get(apiPath(PATH_DISTRICT))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", MatcherUtil.notCollectionEmpty()));
    }





    @Test
    public void testPinCode() throws Exception {
        mockMvc.perform(get(apiPath(PATH_PIN_CODE), "411014")

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }


    @Test
    public void testPinCodeNotFound() throws Exception {
        mockMvc.perform(get(apiPath(PATH_PIN_CODE), "XX")

        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

