package com.github.ankurpathak.api.security;


import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_LOGOUT;
import static com.github.ankurpathak.api.constant.ApiPaths.apiPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {LogoutTests.Initializer.class})
public class LogoutTests extends AbstractRestIntegrationTest<LogoutTests> {

    @Test
    public void doLogout() throws Exception{
        mockMvc
                .perform(logout(apiPath(PATH_LOGOUT)))
                .andDo(print());
    }

}



