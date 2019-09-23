package com.github.ankurpathak.api.batch.test;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;

public class ExtendedJobLauncherTestUtils extends JobLauncherTestUtils {
    @Override
    public void setJob(Job job) {
        super.setJob(job);
    }
}
