package com.choogoomoneyna.choogoomoneyna_be.config;

import com.choogoomoneyna.choogoomoneyna_be.batch.tasklet.CodefWeeklyMissionTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    private final DataSource dataSource;

    public BatchConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager());
        factory.setDatabaseType("mysql");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository());
        return launcher;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory() throws Exception {
        return new JobBuilderFactory(jobRepository());
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory() throws Exception {
        return new StepBuilderFactory(jobRepository(), transactionManager());
    }

    @Bean
    public Job codefWeeklyMissionJob(Step codefWeeklyMissionStep) throws Exception {
        return jobBuilderFactory().get("codefWeeklyMissionJob")
                .start(codefWeeklyMissionStep)
                .build();
    }

    @Bean
    public Step codefWeeklyMissionStep(CodefWeeklyMissionTasklet tasklet) throws Exception {
        return stepBuilderFactory().get("codefWeeklyMissionStep")
                .tasklet(tasklet)
                .build();
    }

    @Bean
    public Job codefDailyMissionJob(Step codefWeeklyMissionStep) throws Exception {
        return jobBuilderFactory().get("codefWeeklyMissionJob")
                .start(codefWeeklyMissionStep)
                .build();
    }

    @Bean
    public Step codefDailyMissionStep(CodefWeeklyMissionTasklet tasklet) throws Exception {
        return stepBuilderFactory().get("codefWeeklyMissionStep")
                .tasklet(tasklet)
                .build();
    }

}
