package com.devied.walletservice.error;


import com.devied.walletservice.util.Error;

public class AmountNotAcceptableException extends BaseError {

    public AmountNotAcceptableException() {
        super(Error.err_203);
    }
}
