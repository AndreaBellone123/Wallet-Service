package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AxerveMethod extends PaymentMethod {

    private String email;

    public AxerveMethod() {
        super("axerve");
    }
}