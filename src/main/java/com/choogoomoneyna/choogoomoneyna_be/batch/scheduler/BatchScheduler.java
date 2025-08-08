package com.choogoomoneyna.choogoomoneyna_be.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job codefWeeklyMissionJob;
    private final Job codefDailyMissionJob;

    public BatchScheduler(JobLauncher jobLauncher, Job codefWeeklyMissionJob, @Qualifier("codefDailyMissionJob") Job codefDailyMissionJob) {
        this.jobLauncher = jobLauncher;
        this.codefWeeklyMissionJob = codefWeeklyMissionJob;

        // 여기에서 해시코드 로그 출력
        System.out.println("🔥 BatchScheduler 해시코드: " + this.hashCode());
        this.codefDailyMissionJob = codefDailyMissionJob;
    }


    //미션 타입1 검증 자동화 스케줄러
    @Scheduled(cron = "0 0 1 * * MON", zone = "Asia/Seoul")
    //@Scheduled(cron = "0 12 09 8 8 *", zone = "Asia/Seoul")
    public void runCodefWeeklyMissionJob() throws Exception {

        log.info("🕒 CODEF_WEEKLY 미션 자동 검증 스케줄러 실행됨 {}", LocalDateTime.now());

        jobLauncher.run(codefWeeklyMissionJob,
                new JobParametersBuilder()
                        .addString("UUID", UUID.randomUUID().toString())
                        .toJobParameters());
    }


    // 미션 타입2 검증 자동화 스케줄러
    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Seoul")
    public void runMissionVerification2Job() throws Exception {

        log.info("🕒 CODEF_DAILY 미션 자동 검증 스케줄러 실행됨 {}", LocalDateTime.now());

        jobLauncher.run(codefDailyMissionJob,
                new JobParametersBuilder()
                        .addString("UUID", UUID.randomUUID().toString())
                        .toJobParameters());
    }
}
