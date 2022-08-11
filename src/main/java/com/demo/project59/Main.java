package com.demo.project59;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner startJob(JobScheduler jobScheduler, AppService appService) {
        return args -> {
            log.info("Cron started!");
            jobScheduler.scheduleRecurrently(Cron.hourly(), () -> appService.doWork3("cron-job1"));
        };
    }
}
