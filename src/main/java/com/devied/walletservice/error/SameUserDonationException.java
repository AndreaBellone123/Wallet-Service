package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;

public class SameUserDonationException extends BaseError {
    public SameUserDonationException() {
        super(Error.err_200);
    }
}
