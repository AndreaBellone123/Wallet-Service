package com.devied.walletservice.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnnotationDrivenListener {
    @EventListener
    public void handleContextStart(CustomSpringEvent customSpringEvent) {
        System.out.println("Success");
    }
}
