package com.github.ankurpathak.api.service.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("social.facebook")
public class FacebookProperties extends SocialProperties {
}
