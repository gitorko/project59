package com.demo.project59.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import com.demo.project59.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    final AppService appService;
    final JobScheduler jobScheduler;
    DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

    @GetMapping("/invoke-job")
    public ResponseEntity invokeJob() {
        String jobTag = "job_" + dtFormat.format(LocalDateTime.now());
        //These will enqueue jobs, will run on a different thread
        jobScheduler.enqueue(() -> {
            appService.doJobWithTag(jobTag);
        });
        jobScheduler.enqueue(() -> {
            appService.doFireAndForgetWork();
        });
        jobScheduler.enqueue(() -> {
            appService.doDelayedWorkWithProgressBar(JobContext.Null);
        });
        log.info("Job enqueued!");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/direct-call")
    public ResponseEntity diretCall() {
        appService.directCall();
        log.info("Job submitted!");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/retry-job")
    public ResponseEntity retryJob() {
        jobScheduler.enqueue(() -> {
            appService.doJobRetry();
        });
        log.info("Retry Job submitted!");
        return ResponseEntity.ok().build();
    }

    /**
     * When there are pool of workers they will pick the jobs from queue and complete them.
     * This distributes the work across many nodes.
     */
    @GetMapping("/many-job/{count}")
    public ResponseEntity submitManyJobs(@PathVariable Integer count) {
        AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < count; i++) {
            String jobTag = "job_" + counter.getAndIncrement();
            jobScheduler.enqueue(() -> {
                appService.doJobWithTag(jobTag);
            });
        }
        log.info("Many Jobs submitted!");
        return ResponseEntity.ok().build();
    }

}

