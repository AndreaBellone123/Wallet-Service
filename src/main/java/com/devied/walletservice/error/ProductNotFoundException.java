package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class ProductNotFoundException extends BaseError {

    public ProductNotFoundException() {
        super(Error.err_002);
    }
}
