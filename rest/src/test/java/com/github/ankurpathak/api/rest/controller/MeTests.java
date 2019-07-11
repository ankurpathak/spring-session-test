package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import static com.github.ankurpathak.api.constant.RequestMappingPaths.PATH_GET_ME;
import static com.github.ankurpathak.api.constant.RequestMappingPaths.apiPath;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {MeTests.Initializer.class})
public class MeTests extends AbstractRestIntegrationTest<MeTests> {



    @Test
    //@WithUserDetails("ankurpathak@live.in")
    public void getMe() throws Exception {
        mockMvc.perform(get(apiPath(PATH_GET_ME))
                .with(authentication(token("ankurpathak@live.in")))

        )
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.id", greaterThan(1)));
    }
}
