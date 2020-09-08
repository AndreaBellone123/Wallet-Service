package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class PaymentMethodNotFoundException extends BaseError{

    public PaymentMethodNotFoundException() {
        super(Error.err_105);
    }
}
