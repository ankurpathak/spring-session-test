package com.github.ankurpathak.api;

import com.github.ankurpathak.api.batch.test.ExtendedJobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;

public class AbstractBatchIntegrationTest<SELF extends AbstractBatchIntegrationTest<SELF>>  extends AbstractIntegrationTest<SELF>{

    @Autowired
    protected GenericApplicationContext applicationContext;

    protected ExtendedJobLauncherTestUtils jobLauncherTestUtils;

}
