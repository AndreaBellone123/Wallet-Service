package com.devied.walletservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Not enough tokens, buy some in order to donate")
public class InsufficientFundsException extends Exception {
}
