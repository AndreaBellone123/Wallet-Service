package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class EmptyCartException extends BaseError {
    public EmptyCartException() {
        super(Error.err_205);
    }
}
