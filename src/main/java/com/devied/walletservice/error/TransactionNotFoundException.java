package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class TransactionNotFoundException extends BaseError {

    public TransactionNotFoundException() {
        super(Error.err_003);
    }
}
