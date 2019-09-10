package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.AbstractBatchIntegrationTest;
import com.github.ankurpathak.api.NonRestCmdApplication;
import com.github.ankurpathak.api.config.test.MongoConfig;
import com.github.ankurpathak.api.config.test.RedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NonRestCmdApplication.class, MongoConfig.class, RedisConfig.class})
@ActiveProfiles("test")
public class ProductCsvTaskTests extends AbstractBatchIntegrationTest<ProductCsvTaskTests> {

   // @Autowired
   // private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testLaunch(){
       // Job job = jobLauncherTestUtils.getJob();

        System.out.println();
        System.out.println();
    }
}
