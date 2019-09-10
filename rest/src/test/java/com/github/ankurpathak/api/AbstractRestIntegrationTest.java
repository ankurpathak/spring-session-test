package com.github.ankurpathak.api;

import com.github.ankurpathak.api.security.DomainContextRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class AbstractRestIntegrationTest<SELF extends AbstractRestIntegrationTest<SELF>> extends AbstractIntegrationTest<SELF> {

    //@RegisterExtension
    @Rule
    public DomainContextRule domainContextRule = new DomainContextRule("103.51.209.45");

    @Autowired
    protected MockMvc mockMvc;

}
