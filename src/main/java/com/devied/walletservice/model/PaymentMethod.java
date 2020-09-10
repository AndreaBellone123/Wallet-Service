package com.devied.walletservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PaymentMethod {

    private UUID uuid;
    private String method;
    private boolean payInMethod;
    private boolean payOutMethod;

    public PaymentMethod(String method) {

        this.method = method;
        this.uuid = UUID.randomUUID();
    }
}
