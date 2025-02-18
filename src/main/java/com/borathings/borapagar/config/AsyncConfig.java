package com.borathings.borapagar.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean
	public Executor backgroundExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsyncThread-");
		executor.setTaskDecorator(addSecurityContext());
		return executor;
	}

	TaskDecorator addSecurityContext() {
		return runnable -> {
			SecurityContext context = SecurityContextHolder.getContext();
			return () -> {
				try {
					SecurityContextHolder.setContext(context);
					runnable.run();
				} finally {
					SecurityContextHolder.clearContext();
				}
			};
		};
	}
}
