package com.ankurpathak.springsessiontest;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "alphaCodeToCallingCodes")
public class CountryCacheService {

    private final ICountryService service;

    public CountryCacheService(ICountryService service) {
        this.service = service;
    }


    @CachePut(key = "#alphaCode")
    public List<String> updateAlphaCodeToCallingCodes(String alphaCode) {
        return service.alphaCodeToCallingCodes(alphaCode);
    }


    @CacheEvict(key = "#alphaCode")
    public void evictAlphaCodeToCallingCodes(String alphaCode) {
    }


    @Cacheable(key = "#alphaCode")
    public List<String> alphaCodeToCallingCodes(String alphaCode) {
        return service.alphaCodeToCallingCodes(alphaCode);
    }

}
