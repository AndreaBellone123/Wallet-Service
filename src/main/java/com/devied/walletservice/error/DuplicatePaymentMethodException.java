package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class DuplicatePaymentMethodException extends BaseError {
    public DuplicatePaymentMethodException() {
        super(Error.err_204);
    }
}
