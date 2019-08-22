package com.github.ankurpathak.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.mongo.MongoDataRule;
import com.github.ankurpathak.api.redis.RedisDataRule;
import com.github.ankurpathak.api.security.DomainContextRule;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.ISchemaService;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

public class AbstractRestIntegrationTest<SELF extends AbstractRestIntegrationTest<SELF>> {

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


    protected UsernamePasswordAuthenticationToken token(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return token;
    }
}
