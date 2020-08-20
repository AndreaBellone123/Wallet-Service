package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String email;
    private String paypalEmail;
    private int earned;
    private int bought;
    private int total;
}
