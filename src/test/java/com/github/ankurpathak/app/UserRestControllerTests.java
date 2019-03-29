package com.github.ankurpathak.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.ankurpathak.app.RequestMappingPaths.PATH_GET_ME;
import static com.github.ankurpathak.app.RequestMappingPaths.apiPath;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    private final CustomUserDetailsService userDetailsService;


    @Autowired
    public UserRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, MongoTemplate mongoTemplate, CustomUserDetailsService userDetailsService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
        this.userDetailsService = userDetailsService;
    }


   // @RegisterExtension
   // public DomainContextBeforeEachExtension domainContextBeforeEachExtension = new DomainContextBeforeEachExtension("103.51.209.45");

    @RegisterExtension
    public MongoSetUpExtension<UserRestControllerTests> mongoSetUpExtension = new MongoSetUpExtension<>(this);

    @Test
    //@WithUserDetails("ankurpathak@live.in")
    public void getMe()throws Exception{
        mockMvc.perform(get(apiPath(PATH_GET_ME))
                .with(authentication(token("ankurpathak@live.in")))

                )
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.id", greaterThan(1)));
    }


    private UsernamePasswordAuthenticationToken token(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return token;
    }

}
