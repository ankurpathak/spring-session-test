package com.github.ankurpathak.api;

import com.github.ankurpathak.api.service.IBankEtlService;
import com.github.ankurpathak.api.service.ICityEtlService;
import com.github.ankurpathak.api.service.ICountryEtlService;
import com.github.ankurpathak.api.service.ISchemaService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class EtlCmdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlCmdApplication.class, args);
    }

}

@Component
class ApplicationRunnerImpl implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRunnerImpl.class);


    private final ICityEtlService cityEtlService;
    private final IBankEtlService bankEtlService;
    private final ISchemaService schemaService;
    private final ICountryEtlService countryEtlService;


    public ApplicationRunnerImpl(ICityEtlService cityEtlService, IBankEtlService bankEtlService, ISchemaService schemaService, ICountryEtlService countryEtlService) {
        this.cityEtlService = cityEtlService;
        this.bankEtlService = bankEtlService;
        this.schemaService = schemaService;
        this.countryEtlService = countryEtlService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (ArrayUtils.contains(args, "bank")) {
            try {
                bankEtlService.process();
            } catch (Exception ex) {
                LogUtil.logStackTrace(log, ex);
            }

        }

        if (ArrayUtils.contains(args, "city")) {
            try {
                cityEtlService.process();
            } catch (Exception ex) {
                LogUtil.logStackTrace(log, ex);
            }

        }
        if (ArrayUtils.contains(args, "country")) {
            try {
                countryEtlService.process();
            } catch (Exception ex) {
                LogUtil.logStackTrace(log, ex);
            }

        }

        if (ArrayUtils.contains(args, "view")) {
            try {
                schemaService.createViews();
            } catch (Exception ex) {
                LogUtil.logStackTrace(log, ex);
            }

        }

    }
}

