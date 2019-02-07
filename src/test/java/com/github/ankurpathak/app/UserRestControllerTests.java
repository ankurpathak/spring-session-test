package com.github.ankurpathak.app;

import com.github.ankurpathak.app.service.ISequenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @RegisterExtension
    public DomainContextBeforeEachExtension domainContextBeforeEachExtension = new DomainContextBeforeEachExtension("103.51.209.45");

    @RegisterExtension
    public MongoCleanUpExtension mongoCleanUpExtension = new MongoCleanUpExtension(this, Sequence.class,User.class, Documents.Role.class);

    @Test
    public void getMe()throws Exception{
        mockMvc.perform(get(apiPath(PATH_GET_ME)).with(authentication(token("7385500660"))))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.id", greaterThan(1)));
    }


    private UsernamePasswordAuthenticationToken token(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return token;
    }





   // @TestConfiguration
    public static class TestConfig {

        @Bean
        public LoginContextRefreshedListener loginContextRefreshedListener(ISequenceService sequenceService, IUserService userService, IRoleService roleService, ITokenService tokenService, PasswordEncoder passwordEncoder){
            return new LoginContextRefreshedListener(sequenceService, userService, roleService, passwordEncoder);
        }
    }
}
