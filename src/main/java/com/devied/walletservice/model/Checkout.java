package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Checkout {

    private String paymentId;
    private String token;
    private String payerId;
    private String url;
}
