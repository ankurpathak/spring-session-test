package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.service.IpService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Optional;

@Service
public class MaxmindGeoIp2Service implements IpService {
    @Autowired
    private DatabaseReader databaseReader;


    @Override
    public Optional<String> ipToCountryAlphaCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = databaseReader.city(ipAddress);
            return Optional.of(response.getCountry().getIsoCode());
        } catch (Exception ex){
            return Optional.of("in");
        }
    }
}
