package com.github.ankurpathak.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.mongo.MongoDataRule;
import com.github.ankurpathak.api.redis.RedisDataRule;
import com.github.ankurpathak.api.security.DomainContextRule;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.ISchemaService;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.BindMode;

public class AbstractRestIntegrationTest<SELF extends AbstractRestIntegrationTest<SELF>> {

    //@RegisterExtension
    @ClassRule
    public static MongoDbContainer mongo = new MongoDbContainer()
        .withCommand("--replSet rs");

    //@RegisterExtension
    @ClassRule
    public static RedisContainer redis = new RedisContainer();
           // .withClasspathResourceMapping("redis.conf", "/usr/local/etc/redis/redis.conf", BindMode.READ_ONLY)
           // .withCommand("redis-server","/usr/local/etc/redis/redis.conf");;


    //@RegisterExtension
    @Rule
    public TestRule mongoDataRule(){
        return new MongoDataRule<>(this);
    }



    //@RegisterExtension
    @Rule
    public TestRule redisDataRule(){
        return new RedisDataRule<>(this);
    }


    //@RegisterExtension
    @Rule
    public DomainContextRule domainContextRule = new DomainContextRule("103.51.209.45");

    @BeforeClass
    public static void setUpAll () throws Exception{
        mongo.execInContainer("/bin/bash", "-c", "mongo --eval 'printjson(rs.initiate())' --quiet");
        mongo.execInContainer("/bin/bash", "-c",
                "until mongo --eval \"printjson(rs.isMaster())\" | grep ismaster | grep true > /dev/null 2>&1;do sleep 1;done");
    }


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
    @Autowired
    protected ISchemaService schemaService;


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
