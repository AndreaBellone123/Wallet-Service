package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class InsufficientFundsException extends BaseError {
    public InsufficientFundsException() {
        super(Error.err_201);
    }
}
