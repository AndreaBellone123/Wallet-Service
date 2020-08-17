package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class CartInForbiddenStatusException extends BaseError {

    public CartInForbiddenStatusException() {
        super(Error.err_202);
    }
}
