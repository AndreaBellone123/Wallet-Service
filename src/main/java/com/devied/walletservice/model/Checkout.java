package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Checkout {

    private String paymentId;
    private String token;
    private String payerId;
    private String url;
}
