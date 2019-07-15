package com.github.ankurpathak.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.mongo.MongoDataRule;
import com.github.ankurpathak.api.redis.RedisDataRule;
import com.github.ankurpathak.api.security.DomainContextRule;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

public class AbstractRestIntegrationTest<SELF extends AbstractRestIntegrationTest<SELF>> {

    //@RegisterExtension
    @ClassRule
    public static MongoDbContainer mongo = new MongoDbContainer();

    //@RegisterExtension
    @ClassRule
    public static RedisContainer redis = new RedisContainer();


    //@RegisterExtension
    @Rule
    public MongoDataRule<SELF> mongoDataRule = new MongoDataRule<>(this);

    //@RegisterExtension
    @Rule
    public RedisDataRule<SELF> redisDataRule = new RedisDataRule<>(this);


    //@RegisterExtension
    @Rule
    public DomainContextRule domainContextRule = new DomainContextRule("103.51.209.45");


    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected CustomUserDetailsService userDetailsService;
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    protected RedisTemplate<?, ?> redisTemplate;


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    String.format("spring.data.mongodb.uri=mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort()),
                    String.format("spring.redis.url=redis://%s:%d", redis.getContainerIpAddress(), redis.getPort())
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    protected UsernamePasswordAuthenticationToken token(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return token;
    }
}
