package com.devied.walletservice.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalParameters {

    private String paymentId;
    private String token;
    private String payerId;

}
