package com.github.ankurpathak.app;


import com.github.ankurpathak.app.constant.Model;
import com.github.ankurpathak.app.service.IRoleService;
import com.github.ankurpathak.app.service.ISequenceService;
import com.github.ankurpathak.app.service.ITokenService;
import com.github.ankurpathak.app.service.IUserService;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class LogoutTests {


    @RegisterExtension
    public MongoSetUpExtension mongoSetUpExtension = new MongoSetUpExtension(this);


}
