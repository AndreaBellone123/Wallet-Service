package com.devied.walletservice.util;

import lombok.Getter;

@Getter
public enum Error {
    err_000("Unknown Error", 500),
    err_001("User Not Found", 404),
    err_002("Product Not Found", 404),
    err_003("Transaction Not Found", 404),
    err_004("Wallet Not Found", 404),
    err_100("Invalid Amount", 406),
    err_200("Cannot donate tokens to yourself", 400),
    err_201("Not enough tokens, buy some in order to donate", 400),
    err_202("Cart in forbidden status", 400);

    private String message;
    private int code;

    Error(String message, int code){
        this.message = message;
        this.code = code;
    }

}
