package com.devied.walletservice.event;

import org.springframework.context.event.*;
import org.springframework.context.annotation.*;
import org.springframework.core.task.*;
@Configuration
public class AsynchronousSpringEventsConfig{
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }
}