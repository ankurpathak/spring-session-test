package com.github.ankurpathak.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
public class JerseyClientConfig {
    @Bean
    public Client client(){
        return ClientBuilder.newClient();
    }
}
