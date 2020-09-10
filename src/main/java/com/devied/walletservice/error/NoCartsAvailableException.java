package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class NoCartsAvailableException extends BaseError {

    public NoCartsAvailableException() {
        super(Error.err_107);
    }
}
