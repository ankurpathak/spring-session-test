package com.github.ankurpathak.app;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("social.google")
public class GoogleProperties extends SocialProperties {
}
