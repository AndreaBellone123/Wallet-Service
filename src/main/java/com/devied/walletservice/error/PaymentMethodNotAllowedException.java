package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class PaymentMethodNotAllowedException extends BaseError {
    public PaymentMethodNotAllowedException() {
        super(Error.err_207);
    }
}
