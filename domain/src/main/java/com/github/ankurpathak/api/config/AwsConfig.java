package com.github.ankurpathak.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class AwsConfig {
    @Bean
    public AwsCredentials awsCredentials() throws IOException {
        ClassPathResource aswPropertiesResource = new ClassPathResource("AwsCredentials.properties");
        Properties awsProp = new Properties();
        awsProp.load(aswPropertiesResource.getInputStream());
        return AwsBasicCredentials.create(awsProp.getProperty("accessKey", ""), awsProp.getProperty("secretKey", ""));
    }

    @Bean
    public S3Client s3(AwsCredentials awsCredentials){
        return S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.AP_SOUTH_1).build();
    }
}
