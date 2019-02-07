package com.github.ankurpathak.app;


import com.github.ankurpathak.app.service.ISequenceService;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class LogoutTests {


    @RegisterExtension
    public MongoCleanUpExtension mongoCleanUpExtension = new MongoCleanUpExtension(this, Sequence.class,User.class, Documents.Role.class);






    //@TestConfiguration
    public static class TestConfig {

        @Bean
        public LoginContextRefreshedListener loginContextRefreshedListener(ISequenceService sequenceService, IUserService userService, IRoleService roleService, ITokenService tokenService, PasswordEncoder passwordEncoder){
            return new LoginContextRefreshedListener(sequenceService, userService, roleService, passwordEncoder);
        }
    }
}
