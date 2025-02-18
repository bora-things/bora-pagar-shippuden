package com.borathings.borapagar.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean
    public Executor backgroundExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // TODO: Rever numeros (?)
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
