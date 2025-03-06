package com.slavchev.scheduledcalculations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;

@Configuration
public class TestConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        return new SimpleAsyncTaskScheduler();
    }
}
