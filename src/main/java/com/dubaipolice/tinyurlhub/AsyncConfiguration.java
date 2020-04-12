package com.dubaipolice.tinyurlhub;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.dubaipolice.tinyurlhub.controller.URLAliasController;

@Configuration
@EnableAsync
public class AsyncConfiguration {
	private static Logger LOGGER = LoggerFactory.getLogger(URLAliasController.class);
    @Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        LOGGER.debug("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("TinyUrlThread-");
        executor.initialize();
        return executor;
    }
}
