package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class UserNotFoundException extends BaseError {

    public UserNotFoundException() {
        super(Error.err_101);
    }
}
