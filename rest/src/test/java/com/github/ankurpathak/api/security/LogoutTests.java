package com.github.ankurpathak.api.security;


import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import com.github.ankurpathak.api.SpringSessionTestApplication;
import com.github.ankurpathak.api.config.MongoConfig;
import com.github.ankurpathak.api.config.RedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_LOGOUT;
import static com.github.ankurpathak.api.constant.ApiPaths.apiPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MongoConfig.class, RedisConfig.class, SpringSessionTestApplication.class})
@ActiveProfiles("test")
public class LogoutTests extends AbstractRestIntegrationTest<LogoutTests> {

    @Test
    public void doLogout() throws Exception{
        mockMvc
                .perform(logout(apiPath(PATH_LOGOUT)))
                .andDo(print());
    }

}



