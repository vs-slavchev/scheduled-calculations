package com.slavchev.scheduledcalculations.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@ConditionalOnProperty(
        value = "scheduling.enabled", havingValue = "true", matchIfMissing = true
)
@Configuration
@EnableScheduling
public class TaskExecutorConfig {

    @Bean
    public TaskScheduler taskScheduler(@Value("${executor.poolsize:2}") int poolSize) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        return scheduler;
    }
}
