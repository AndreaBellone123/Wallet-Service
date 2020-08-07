package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String email;
    private int earnedTokens;
    private int boughtTokens;
    private boolean isAdmin;
    private double availableFunds;
}
