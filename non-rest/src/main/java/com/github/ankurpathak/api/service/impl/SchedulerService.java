package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.ServiceException;
import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.service.ISchedulerService;
import com.github.ankurpathak.api.util.LogUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class SchedulerService implements ISchedulerService {
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void scheduleJob(final Class<? extends Job> job, final String name, final String group, final JobDataMap data, String cron, Instant start, Instant end){
        require(job, notNullValue());
        require(name, notNullValue());
        require(group, notNullValue());
        require(data, notNullValue());
        require(cron, notNullValue());
        require(start, notNullValue());
        require(end, notNullValue());
        JobDetail detail = JobBuilder.newJob(job)
                .withIdentity(name, group)
                .usingJobData(data)
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(detail)
                .withIdentity(name, group)
                .startAt(Date.from(start))
                .endAt(Date.from(end))
                .withSchedule(CronScheduleBuilder.cronSchedule(cron).inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC)))
                .build();
        try {
            scheduler.scheduleJob(detail, trigger);
        } catch(ObjectAlreadyExistsException ex){
           throw new FoundException(ex, List.of(new FoundDto("(name,group)", String.format("(%s,%s)", name, group))), Job.class);
        } catch (SchedulerException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
    }
    @Override
    public void deleteJob(String name, String group){
        try {
            scheduler.deleteJob(JobKey.jobKey(name, group));
        } catch (SchedulerException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void rescheduleJob(String name, String group, String cron, Instant start, Instant end){
        try {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(scheduler.getJobDetail(JobKey.jobKey(name, group)))
                    .withIdentity(name, group)
                    .startAt(DateBuilder.translateTime(Date.from(start), TimeZone.getDefault(), TimeZone.getTimeZone(ZoneOffset.UTC)))
                    .endAt(DateBuilder.translateTime(Date.from(end), TimeZone.getDefault(), TimeZone.getTimeZone(ZoneOffset.UTC)))
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron).inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC)))
                    .build();
            scheduler.rescheduleJob(TriggerKey.triggerKey(name, group), trigger);
        } catch (SchedulerException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
