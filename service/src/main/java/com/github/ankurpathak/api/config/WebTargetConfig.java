package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.service.impl.CountryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

@Configuration
public class WebTargetConfig {

    private final Client client;

    public WebTargetConfig(Client client) {
        this.client = client;
    }


    @Bean
    @Named("countryBaseTarget")
    public WebTarget countryBaseTarget() {
        return client.target(CountryService.BASE_URL);
    }

    @Bean
    @Named("countryByAlphaTarget")
    public WebTarget countryByAlphaTarget(@Named("countryBaseTarget") WebTarget countryBaseTarget){
        return countryBaseTarget.path("alpha");
    }
}
