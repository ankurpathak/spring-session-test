package com.github.ankurpathak.api.config;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class GeoIp2Config {
    @Bean
    public DatabaseReader databaseReader() throws Exception{
        ClassPathResource resource =  new ClassPathResource("GeoLite2-City.mmdb");
        return new DatabaseReader.Builder(resource.getInputStream()).withCache(new CHMCache()).build();
    }
}
