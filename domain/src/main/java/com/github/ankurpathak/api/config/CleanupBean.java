package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.annotation.Dev;
import com.github.ankurpathak.api.annotation.Prod;
import com.github.ankurpathak.api.annotation.Stag;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@Dev
@Prod
@Stag
public class CleanupBean implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(CleanupBean.class);
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private JedisConnectionFactory redisConnectionFactory;

    @Override
    public void destroy() throws Exception {
        LogUtil.logInfo(log, "Starting shutdown process - cleanup");
        if(mongoClient != null){
            mongoClient.close();
        }
        if(redisConnectionFactory != null){
            redisConnectionFactory.destroy();
        }
        LogUtil.logInfo(log, "Finishing shutdown process - cleanup");
    }
}
