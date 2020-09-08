package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class PaypalUserNotFoundException extends BaseError {

    public PaypalUserNotFoundException() {
        super(Error.err_106);
    }
}
