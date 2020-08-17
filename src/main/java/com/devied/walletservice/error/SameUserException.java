package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class SameUserException extends BaseError {
    public SameUserException() {
        super(Error.err_200);
    }
}
