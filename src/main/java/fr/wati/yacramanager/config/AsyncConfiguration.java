package fr.wati.yacramanager.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import fr.wati.yacramanager.async.ExceptionHandlingAsyncTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Autowired
    private Environment environment;


    @Override
    @Bean
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(environment.getProperty("spring.async.corePoolSize", Integer.class, 2));
        executor.setMaxPoolSize(environment.getProperty("spring.async.maxPoolSize", Integer.class, 50));
        executor.setQueueCapacity(environment.getProperty("spring.async.queueCapacity", Integer.class, 10000));
        executor.setThreadNamePrefix("yacra-executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }
}
