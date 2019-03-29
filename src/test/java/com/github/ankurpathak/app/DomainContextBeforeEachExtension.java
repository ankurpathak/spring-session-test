package com.github.ankurpathak.app;

import com.github.ankurpathak.app.security.core.DomainContextHolder;
import com.github.ankurpathak.app.security.dto.DomainContext;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DomainContextBeforeEachExtension implements BeforeEachCallback {
    private final String ip;

    public DomainContextBeforeEachExtension(String ip) {
        this.ip = ip;
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        DomainContext context = mock(DomainContext.class);
        doReturn(ip).when(context).getRemoteAddress();
        DomainContextHolder.setContext(context);
    }
}
