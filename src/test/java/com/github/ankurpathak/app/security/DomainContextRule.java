package com.github.ankurpathak.app.security;

import com.github.ankurpathak.app.security.core.DomainContextHolder;
import com.github.ankurpathak.app.security.dto.DomainContext;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DomainContextRule implements BeforeEachCallback, TestRule {
    private final String ip;

    public DomainContextRule(String ip) {
        this.ip = ip;
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
       beforeEach();
    }


    public void beforeEach(){
        DomainContext context = mock(DomainContext.class);
        doReturn(ip).when(context).getRemoteAddress();
        DomainContextHolder.setContext(context);
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                beforeEach();
                statement.evaluate();
            }
        };
    }
}
