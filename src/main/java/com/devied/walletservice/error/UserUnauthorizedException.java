package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class UserUnauthorizedException extends BaseError {

    public UserUnauthorizedException() {
        super(Error.err_301);
    }
}
