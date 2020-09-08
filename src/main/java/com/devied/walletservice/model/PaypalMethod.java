package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalMethod extends PaymentMethod {

    private String email;

    public PaypalMethod() {
        super("paypal");
    }
}
