package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public abstract class PaymentMethod {

    private UUID uuid;
    private String method;

    public PaymentMethod(String method){
        this.method = method;
        this.uuid = UUID.randomUUID();
    }
}
