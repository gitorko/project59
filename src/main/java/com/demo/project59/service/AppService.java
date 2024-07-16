package com.demo.project59.service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.demo.project59.domain.Customer;
import com.demo.project59.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppService {

    final CustomerRepository customerRepository;
    final JobScheduler jobScheduler;

    @SneakyThrows
    public void doDelayedWork() {
        log.info("Running doWork");
        TimeUnit.SECONDS.sleep(1);
        log.info("Completed doWork");
    }

    @Job(name = "doJob [jobTag: %0]")
    @SneakyThrows
    public void doJobWithTag(String jobTag) {
        log.info("Running doJob");
        TimeUnit.MINUTES.sleep(1);
        log.info("Completed doJob");
    }

    /**
     * Once the job fails it can be picked for next retry by any other server in the pool.
     * So don't use AtomicInteger to track count
     */
    @Job(name = "doJobRetry", retries = 3)
    @SneakyThrows
    public void doJobRetry() {
        log.info("Running doJobRetry");
        Customer customer = customerRepository.findById(200l).orElseThrow();
        customer.setInvokeCount(customer.getInvokeCount() + 1);
        customerRepository.save(customer);
        log.info("Updated customer invoke count!");
        if (customer.getInvokeCount() < 3) {
            throw new RuntimeException("Will not work first 2 times");
        }
        log.info("Completed doJobRetry");
    }

    @Job(name = "doFireAndForgetWork")
    @SneakyThrows
    public void doFireAndForgetWork() {
        log.info("Running fireForget");
        TimeUnit.MINUTES.sleep(1);
        log.info("Completed fireForget");
    }

    @Job(name = "hourlyJob")
    @Recurring(id = "hourlyJob", cron = "0 * * * *", zoneId = "Asia/Kolkata")
    @SneakyThrows
    public void doHourlyWork() {
        log.info("Running hourlyJob");
        TimeUnit.SECONDS.sleep(5);
        log.info("Completed hourlyJob");
    }

    @Job(name = "doDelayedWorkWithProgressBar")
    @SneakyThrows
    public void doDelayedWorkWithProgressBar(JobContext jobContext) {
        JobDashboardProgressBar bar = jobContext.progressBar(100);
        for (int i = 0; i < 10; i++) {
            log.info("Running longJob");
            //progress by 10% each time
            bar.setProgress(10 * i);
            TimeUnit.SECONDS.sleep(5);
            log.info("Completed fireForget");
        }
    }

    public void directCall() {
        //Recurring
        jobScheduler.scheduleRecurrently(Cron.hourly(), () -> {
            doDelayedWork();
        });

        //Fire Forget
        jobScheduler.enqueue(() -> {
            doDelayedWork();
        });

        //Delay job, scheduled to run in future
        jobScheduler.schedule(Instant.now().plusSeconds(30), () -> {
            doDelayedWork();
        });
    }
}
