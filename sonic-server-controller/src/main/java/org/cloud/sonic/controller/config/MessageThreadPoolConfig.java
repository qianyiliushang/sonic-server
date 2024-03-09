package org.cloud.sonic.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MessageThreadPoolConfig {

    @Bean(destroyMethod = "destroy")
    public ThreadPoolTaskExecutor messageThreadPool() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int MAX_CORE_SIZE = 8;
        threadPoolExecutor.setCorePoolSize(MAX_CORE_SIZE);
        threadPoolExecutor.setMaxPoolSize(MAX_CORE_SIZE * 2);
        int QUEUE_SIZE = 100000;
        threadPoolExecutor.setQueueCapacity(QUEUE_SIZE);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.setThreadNamePrefix("message-");
        threadPoolExecutor.setKeepAliveSeconds(60);
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
