package com.github.ankurpathak.api;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CmdJob extends QuartzJobBean {
    @Autowired
    private MessageSource messageSource;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Instant.now() + ": Hello Quartz Job");
    }
}
