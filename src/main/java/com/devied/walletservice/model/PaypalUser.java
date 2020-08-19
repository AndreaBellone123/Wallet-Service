package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalUser {
    private String accessToken;
    private String email;
}
