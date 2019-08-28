package com.github.ankurpathak.api.config.test;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@Test
public class CleanupBean implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(CleanupBean.class);

    @Autowired(required = false)
    private RedisContainer redis;
    @Autowired(required = false)
    private MongoDbContainer mongo;
    @Autowired(required = false)
    private MongoClient mongoClient;
    @Autowired(required = false)
    private JedisConnectionFactory redisConnectionFactory;


    @Override
    public void destroy() throws Exception {
        LogUtil.logInfo(log, "Starting shutdown process - cleanup");
        if(mongoClient != null){
            mongoClient.close();
        }
        if(mongo != null){
            mongo.close();
        }
        if(redisConnectionFactory != null){
            redisConnectionFactory.destroy();
        }
        if(redis != null){
            redis.close();
        }
        LogUtil.logInfo(log, "Finishing shutdown process - cleanup");
    }
}
