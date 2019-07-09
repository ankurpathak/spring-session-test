package com.github.ankurpathak.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.mongo.MongoDataRule;
import com.github.ankurpathak.app.security.service.CustomUserDetailsService;
import com.github.ankurpathak.app.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.app.testcontainer.redis.RedisContainer;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.ankurpathak.app.constant.RequestMappingPaths.PATH_GET_ME;
import static com.github.ankurpathak.app.constant.RequestMappingPaths.apiPath;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ATests.Initializae.class})
public class ATests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    //@RegisterExtension
    @ClassRule
    public static MongoDbContainer mongo = new MongoDbContainer();

    //@RegisterExtension
    @ClassRule
    public static RedisContainer redis = new RedisContainer();

//    @RegisterExtension
    @Rule
    public MongoDataRule mongoDataRule = new MongoDataRule(this);


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


    private UsernamePasswordAuthenticationToken token(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return token;
    }


    // @RegisterExtension
    // public DomainContextRule domainContextRule = new DomainContextRule("103.51.209.45");
    // ;


    public static class Initializae implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.uri=" + String.format("mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort()),
                    "spring.redis.url=" + String.format("redis://%s:%d", redis.getContainerIpAddress(), redis.getPort())
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


}
