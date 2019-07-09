package com.github.ankurpathak.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /*@Override
    public Executor getAsyncExecutor() {
        return new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
    }

    */


     /*

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(){
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setTargetClass(SecurityContextHolder.class);
        bean.setTargetMethod("setStrategyName");
        bean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        return bean;
    }

    */


}
