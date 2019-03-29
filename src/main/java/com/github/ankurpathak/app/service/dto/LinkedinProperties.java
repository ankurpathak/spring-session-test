package com.github.ankurpathak.app.service.dto;


import com.github.ankurpathak.app.SocialProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("social.linkedin")
public class LinkedinProperties extends SocialProperties {
}
