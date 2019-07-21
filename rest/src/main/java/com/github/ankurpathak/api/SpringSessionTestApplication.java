package com.github.ankurpathak.api;

import com.github.ankurpathak.api.constant.CityEtlConstants;
import com.github.ankurpathak.api.domain.repository.mongo.ICityRepository;
import com.github.ankurpathak.api.service.IBankIfscEtlService;
import com.github.ankurpathak.api.service.ICityEtlService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}


@Component
class ApplicationRunnerImpl implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRunnerImpl.class);


    private final ICityEtlService cityEtlService;
    private final IBankIfscEtlService bankIfscEtlService;

    @Autowired
    private ICityRepository cityRepository;

    public ApplicationRunnerImpl(ICityEtlService cityEtlService, IBankIfscEtlService bankIfscEtlService) {
        this.cityEtlService = cityEtlService;
        this.bankIfscEtlService = bankIfscEtlService;
    }


    @Override
    public void run(String... args) throws Exception {
        if(ArrayUtils.contains(args, "bank")){
            try{
                bankIfscEtlService.process();
            }catch (Exception ex){
                LogUtil.logStackTrace(log, ex);
            }

        }

        if(ArrayUtils.contains(args,"city")){
            try{
                cityEtlService.process();
            }catch (Exception ex){
                LogUtil.logStackTrace(log, ex);
            }

        }

        cityRepository.findPinCodes("MAHARASHTRA", "Pune", PageRequest.of(0, 200));
    }
}
