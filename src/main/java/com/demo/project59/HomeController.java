package com.demo.project59;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private AppService appService;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

    @GetMapping("/api/{number}")
    public ResponseEntity isPrime(@PathVariable int number) {
        for (int i = 0; i < number; i++) {
            String jobName = "job_" + format.format(LocalDateTime.now());
            jobScheduler.enqueue(() -> appService.doWork1(jobName));
            jobScheduler.enqueue(() -> appService.doWork2(jobName));
        }
        log.info("Job submitted!");
        return ResponseEntity.ok().build();
    }
}

