package com.devied.walletservice.util;

import lombok.Getter;

@Getter
public enum Error {
    err_000("Unknown Error", 500),
    err_101("User Not Found", 404),
    err_102("Product Not Found", 404),
    err_103("Transaction Not Found", 404),
    err_104("Wallet Not Found", 404),
    err_105("Payment Method Not Found", 404),
    err_106("Paypal User Not Found", 404),
    err_107("No Carts Available",404),
    err_200("Cannot donate tokens to yourself", 400),
    err_201("Not enough tokens, buy some in order to donate", 400),
    err_202("Cart in forbidden status", 400),
    err_203("Invalid Amount", 400),
    err_204("Duplicate Payment Method", 400),
    err_205("Empty Cart", 400),
    err_206("User Already Exists", 400),
    err_207("Unsuppported Payment Method", 400);

    private final String message;
    private final int code;

    Error(String message, int code){
        this.message = message;
        this.code = code;
    }
}
