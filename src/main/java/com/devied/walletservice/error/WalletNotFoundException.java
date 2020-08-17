package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class WalletNotFoundException extends BaseError {

    public WalletNotFoundException() {
        super(Error.err_004);
    }
}
