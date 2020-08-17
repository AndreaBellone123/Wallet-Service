package com.devied.walletservice.error;

import com.devied.walletservice.util.Error;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseError extends Exception {

    private Error error;

    public BaseError(Error error) {
        this.error = error;
    }
}