package com.github.ankurpathak.api.service;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;

public interface ISchedulerService {

    void scheduleJob(Class<? extends Job> job, String name, String group, JobDataMap data, String cron, Instant start, Instant end);

    void deleteJob(String name, String group);

    void rescheduleJob(String name, String group, String cron, Instant start, Instant end);
}
