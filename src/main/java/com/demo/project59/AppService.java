package com.demo.project59;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppService {

    public void doWork1(String job) {
        log.info("doWork1: {}", job);
    }

    @Job(name = "Work2 job name: %0", retries = 2)
    public void doWork2(String job) {
        log.info("doWork2: {}", job);
    }

    @Job(name = "Cron job name: %0", retries = 0)
    public void doWork3(String job) {
        log.info("doWork3: {}", job);
    }
}
